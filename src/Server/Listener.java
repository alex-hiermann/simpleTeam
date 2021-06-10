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
import java.util.HashSet;
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
                    args = BasicFunctionLibrary.getArgs(data, args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                switch (command) {
                    case "createTeam" -> {
                        System.out.println("Teamargs = " + Arrays.toString(args));
                        Team team = new Team(BasicFunctionLibrary.findValueFromArgs("teamname", args),
                                BasicFunctionLibrary.findValueFromArgs("teamdesc", args));
                        User serverUser = Server.users.get(Server.users.indexOf(new User(findValueFromArgs("email", args))));
                        team.members.add(serverUser);   //Add user to team
                        serverUser.myTeams.add(team);   //Add the team to user
                        team.setAdmin(serverUser);      //Make him an admin, because he created the team
                        Server.teams.add(team);         //Finally add the team to the server
                        sendSTRequestToClient("createTeam:" + team + ",teamId=ꠦ" + team.getId() + "ꠦ");
                    }
                    case "getTeams" -> {
                        User serverUser = Server.users.get(Server.users.indexOf(extractUserFromArgs(args)));
                        StringBuilder request = new StringBuilder();
                        ArrayList<Team> userTeams = new ArrayList<>();
                        for (Team team : Server.teams) {
                            if (team.members.contains(serverUser)) {
                                request.append(team).append(";");
                                userTeams.add(team);
                            }
                        }
                        String clientRequest = request.toString();
                        try {
                            sendSTRequestToClient("userTeams:" + clientRequest.substring(0, clientRequest.length() - 1));
                            for (Team userTeam : userTeams) {
                                ArrayList<String> strings = userTeam.getChatroom().generateMessages();
                                for (String msgRequest : strings) {
                                    sendSTRequestToClient("fetchMessage:" + msgRequest);
                                }
                            }
                        } catch (StringIndexOutOfBoundsException ignored) {
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
                        }
                    }
                    case "login" -> {
                        System.out.println("args = " + Arrays.toString(args));
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
                        Message message = new Message(user, BasicFunctionLibrary.findValueFromArgs("messageText", args),
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
                    }
                    case "addUserToTeam" -> {
                        User invitedUser = new User(BasicFunctionLibrary.findValueFromArgs("email", args));
                        System.out.println("invitedUser = " + invitedUser);
                        Team team = new Team(BasicFunctionLibrary.findValueFromArgs("teamname", args),
                                BasicFunctionLibrary.findValueFromArgs("teamdesc", args),
                                Integer.parseInt(BasicFunctionLibrary.findValueFromArgs("teamId", args)));
                        Server.teams.get(Server.teams.indexOf(team)).members.add(Server.users.get(Server.users.indexOf(invitedUser)));
                        try {
                            Server.listeners.get(invitedUser).sendSTRequestToClient("requestTeams");
                        } catch (NullPointerException ignored) {
                            System.err.println("No Listener Found");
                            //When the user isn't online we are ignoring the request
                        }
                    }
                    case "addTask" -> {
                        Task tempTask = new Task(findValueFromArgs("taskName", args),
                                findValueFromArgs("taskDescription", args),
                                LocalDate.parse(findValueFromArgs("taskDue", args)),
                                extractTaskTypeFromText(findValueFromArgs("taskType", args)),
                                extractTaskDifficultyFromText(findValueFromArgs("taskDifficulty", args)));
                        tempTask.setTeam_id(Integer.parseInt(findValueFromArgs("teamId", args)));
                        tempTask.setUser(new User(BasicFunctionLibrary.findValueFromArgs("email", args)));
                        if (getEntryFromLinkedList(Server.teams, new Team(Integer.parseInt(findValueFromArgs("teamId", args))))
                                .tasks.add(tempTask)) {
                            sendSTRequestToClient("taskAddSuccess");
                        } else {
                            sendSTRequestToClient("taskAddFail");
                        }
                    }
                    case "requestUsers" -> {
                        System.out.println("args = " + Arrays.toString(args));
                        int teamId = Integer.parseInt(BasicFunctionLibrary.findValueFromArgs("teamId", args));
                        for (User user : getEntryFromLinkedList(Server.teams, new Team(teamId)).members) {
                            sendSTRequestToClient("fetchedUser:" + user.toString() + ",teamId=ꠦ" + teamId + "ꠦ");
                        }
                    }
                }
            }
        } catch (SocketException e) {
            for (Map.Entry<User, Listener> entry : Server.listeners.entrySet()) {
                if (entry.getValue().equals(this)) {
                    Server.listeners.remove(entry.getKey(), entry.getValue());
                }
            }
        } catch (ParseException | IOException e) {
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