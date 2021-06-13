package Server;

import Client.Chat.Message;
import Client.Task;
import Client.Team;
import Client.User;
import Utils.BasicFunctionLibrary;
import Utils.Configuration;
import Utils.SQLite.SQLiteHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

import static Utils.BasicFunctionLibrary.extractTaskDifficultyFromText;
import static Utils.BasicFunctionLibrary.extractTaskTypeFromText;
import static Utils.BasicFunctionLibrary.extractUserFromArgs;
import static Utils.BasicFunctionLibrary.findValueFromArgs;
import static Utils.BasicFunctionLibrary.getEntryFromLinkedList;

public class Listener implements Runnable {

    Socket socket;

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String data;

            //STRequest handling
            while (!(data = dataInputStream.readUTF()).isEmpty()) {
                String command = data.split(":")[0];
                String[] args = new String[0];
                try {
                    args = BasicFunctionLibrary.getArgs(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                switch (command) {
                    case "createTeam" -> {
                        Team team = new Team(BasicFunctionLibrary.findValueFromArgs("teamname", args),
                                BasicFunctionLibrary.findValueFromArgs("teamdesc", args),
                                new User(BasicFunctionLibrary.findValueFromArgs("adminEmail", args)));

                        Configuration.teamId = SQLiteHandler.retrieveTeamId();
                        team.setId(++Configuration.teamId);

                        User serverUser = Server.users.get(Server.users.indexOf(
                                new User(findValueFromArgs("email", args))));
                        team.members.add(serverUser);   //Add user to team
                        serverUser.myTeams.add(team);   //Add the team to user

                        team.setAdmin(serverUser);      //Make him an admin, because he created the team
                        Server.teams.add(team);         //Finally add the team to the server

                        sendSTRequestToClient("createTeam:" + team + ",teamId=ꠦ" + team.getId() + "ꠦ");
                        SQLiteHandler.addNewTeamToDatabase(team);
                        SQLiteHandler.addUserToTeam(serverUser, team);
                    }
                    case "getTeams" -> {
                        SQLiteHandler.getAllUsers();
                        SQLiteHandler.getAllTeams();
                        SQLiteHandler.getAllMessages();

                        User serverUser = Server.users.get(Server.users.indexOf(extractUserFromArgs(args)));
                        ArrayList<Team> userTeams = new ArrayList<>();

                        for (Team team : Server.teams) {
                            if (team.members.contains(serverUser)) {
                                sendSTRequestToClient("fetchTeam:" + team);
                                userTeams.add(team);
                            }
                        }

                        try {
                            for (Team userTeam : userTeams) {
                                ArrayList<String> strings = userTeam.getChatroom().generateMessages();
                                for (String msgRequest : strings) {
                                    sendSTRequestToClient("fetchMessage:" + msgRequest);
                                }
                            }
                        } catch (StringIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }


                    }
                    // registerUser:email='email',username='username',password='password',name='name',lastname='lastname',birth='age'
                    case "registerUser" -> {
                        User user = extractUserFromArgs(args);
                        if (Server.users.contains(user)) {
                            sendSTRequestToClient("userExists");
                        } else {
                            Configuration.userId = SQLiteHandler.retrieveUserID();
                            user.setId(++Configuration.userId);
                            Server.users.add(user);
                            sendSTRequestToClient("userRegistered");
                            SQLiteHandler.addNewUserToDatabase(user);
                        }
                    }
                    case "login" -> {
                        SQLiteHandler.getAllUsers();
                        User user = new User(
                                BasicFunctionLibrary.findValueFromArgs("email", args),
                                BasicFunctionLibrary.findValueFromArgs("password", args));
                        try {
                            User foundUser = Server.users.get(Server.users.indexOf(user));
                            if (foundUser.getEmail().equalsIgnoreCase(user.getEmail()) && foundUser.getPassword().equals(user.getPassword())) {
                                sendSTRequestToClient("canLogin:" + foundUser);
                                Server.listeners.put(foundUser, this);
                            } else {
                                sendSTRequestToClient("rejectedLogin");
                            }
                        } catch (Exception ignored) {
                            sendSTRequestToClient("rejectedLogin");
                        }
                    }
                    case "sendMessage" -> {
                        User user = new User(BasicFunctionLibrary.findValueFromArgs("email", args));
                        Message message = new Message(getEntryFromLinkedList(Server.users, user), BasicFunctionLibrary.findValueFromArgs("messageText", args),
                                Message.dateFormat.parse(BasicFunctionLibrary.findValueFromArgs("date", args)));
                        Team team = new Team(Integer.parseInt(BasicFunctionLibrary.findValueFromArgs("teamid", args)));
                        Team serverTeam = Server.teams.get(Server.teams.indexOf(team));
                        serverTeam.getChatroom().addMessage(message);
                        for (User teamUser : serverTeam.members) {
                            if (!teamUser.equals(user)) {
                                try {
                                    Server.listeners.get(teamUser).sendSTRequestToClient("fetchMessage:" + message +
                                            ",teamId=ꠦ" + team.getId() + "ꠦ");
                                } catch (NullPointerException ignored) {
                                    //No active user!
                                }
                            }
                        }
                        SQLiteHandler.addNewMessageToDatabase(message, team.getId());
                    }
                    case "addUserToTeam" -> {
                        System.out.println("args = " + Arrays.toString(args));
                        User invitedUser = new User(BasicFunctionLibrary.findValueFromArgs("email", args));
                        Team team = new Team(BasicFunctionLibrary.findValueFromArgs("teamname", args),
                                BasicFunctionLibrary.findValueFromArgs("teamdesc", args),
                                Integer.parseInt(BasicFunctionLibrary.findValueFromArgs("teamId", args)));
                        Server.teams.get(Server.teams.indexOf(team)).members
                                .add(Server.users.get(Server.users.indexOf(invitedUser)));
                        try {
                            Server.listeners.get(invitedUser).sendSTRequestToClient("fetchTeam:" + getEntryFromLinkedList(Server.teams, team));

                        } catch (NullPointerException ignored) {
                            System.err.println("No Listener Found");
                            //When the user isn't online we are ignoring the request
                        }
                        SQLiteHandler.addUserToTeam(getEntryFromLinkedList(Server.users, invitedUser),
                                getEntryFromLinkedList(Server.teams, team));
                    }
                    case "addTask" -> {
                        Task tempTask = new Task(findValueFromArgs("taskName", args),
                                findValueFromArgs("taskDescription", args),
                                LocalDate.parse(findValueFromArgs("taskDue", args)),
                                extractTaskTypeFromText(findValueFromArgs("taskType", args)),
                                extractTaskDifficultyFromText(findValueFromArgs("taskDifficulty", args)));
                        int teamId = Integer.parseInt(findValueFromArgs("teamId", args));
                        tempTask.setTeam_id(teamId);
                        tempTask.setUser(getEntryFromLinkedList(Server.users, new User(BasicFunctionLibrary.findValueFromArgs("email", args))));
                        Configuration.taskId = SQLiteHandler.retrieveTaskId();
                        tempTask.setTaskId(++Configuration.taskId);
                        if (getEntryFromLinkedList(Server.teams, new Team(teamId))
                                .tasks.add(tempTask)) {
                            String clientTaskId = findValueFromArgs("clientTaskId", args);
                            sendSTRequestToClient("taskAddSuccess:taskId=ꠦ" + tempTask.getTaskId() + "ꠦ,teamId=ꠦ" +
                                    teamId + "ꠦ,clientTaskId=ꠦ" +
                                    clientTaskId + "ꠦ");

                            Server.listeners.get(tempTask.getUser()).sendSTRequestToClient("newTaskAssigned");

                            for (User teamUser : getEntryFromLinkedList(Server.teams, new Team(teamId)).members) {
                                try {
                                    Server.listeners.get(teamUser).sendSTRequestToClient("fetchTask:" + tempTask + ",taskId=ꠦ" + tempTask.getTaskId() + "ꠦ");
                                } catch (NullPointerException ignored) {
                                }
                            }


                            SQLiteHandler.addNewTaskToDatabase(tempTask);
                        } else {
                            sendSTRequestToClient("taskAddFail");
                        }
                    }
                    case "requestUsers" -> {
                        SQLiteHandler.getAllUsers();
                        int teamId = Integer.parseInt(BasicFunctionLibrary.findValueFromArgs("teamId", args));
                        for (User user : getEntryFromLinkedList(Server.teams, new Team(teamId)).members) {
                            sendSTRequestToClient("fetchedUser:" + user.toString() + ",teamId=ꠦ" + teamId + "ꠦ");
                        }
                    }
                    case "changeTaskState" -> {
                        SQLiteHandler.getAllTasks();
                        Task.E_TASK_STATE newTaskState = BasicFunctionLibrary.extractTaskStateFromText(
                                BasicFunctionLibrary.findValueFromArgs("newTaskState", args));
                        Task task = new Task(Integer.parseInt(BasicFunctionLibrary.findValueFromArgs("taskId", args)));
                        int teamId = Integer.parseInt(BasicFunctionLibrary.findValueFromArgs("teamId", args));
                        BasicFunctionLibrary.getEntryFromLinkedList(getEntryFromLinkedList(Server.teams, new Team(teamId)).tasks, task).setState(newTaskState);
                        System.out.println(task.getTaskId());
                        System.out.println(newTaskState);
                        SQLiteHandler.updateTaskState(task.getTaskId(), newTaskState);
                    }

                    case "fetchTasks" -> {
                        SQLiteHandler.getAllTasks();
                        Team team = new Team(Integer.parseInt(findValueFromArgs("teamId", args)));
                        try {
                            Team team1 = Server.teams.get(Server.teams.indexOf(team));
                            LinkedList<Task> tasks = team1.tasks;
                            for (Task task : tasks) {
                                sendSTRequestToClient("fetchTask:" + task + ",taskId=ꠦ" + task.getTaskId() + "ꠦ");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (
                SocketException e) {
            for (Map.Entry<User, Listener> entry : Server.listeners.entrySet()) {
                if (entry.getValue().equals(this)) {
                    Server.listeners.remove(entry.getKey(), entry.getValue());
                }
            }
        } catch (ParseException |
                IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return socket.getInetAddress().toString() + ":" + socket.getPort();
    }

    public Listener(Socket s) {
        socket = s;
    }

    public void sendSTRequestToClient(String message) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}