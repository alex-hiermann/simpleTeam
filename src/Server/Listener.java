package Server;

import Client.Chat.Chatroom;
import Client.Chat.Message;
import Client.User;
import Client.Team;
import Utils.BasicFunctionLibrary;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import static Utils.BasicFunctionLibrary.extractUserFromArgs;
import static Utils.BasicFunctionLibrary.findValueFromArgs;

public class Listener implements Runnable {

    static Socket socket;

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String data = "";

            //STRequest handling
            synchronized (this) {
                while (!(data = dataInputStream.readUTF()).isEmpty()) {
                    String command = data.split(":")[0];
                    String[] args = new String[0];
                    try {
                        String[] temp = data.split(":");
                        StringBuilder arguments = new StringBuilder();
                        for (int i = 1; i < temp.length; i++) {
                            if (i == 1) {
                                arguments.append(temp[i]);
                            } else {
                                arguments.append(":").append(temp[i]);
                            }
                        }
                        args = arguments.toString().split(",");
                    } catch (Exception ignored) {
                    }
                    switch (command) {
                        case "createTeam" -> {
                            Team team = new Team(BasicFunctionLibrary.findValueFromArgs("teamname", args), BasicFunctionLibrary.findValueFromArgs("teamdesc", args));
                            User serverUser = Server.users.get(Server.users.indexOf(extractUserFromArgs(args)));
                            team.members.add(serverUser);   //Add user to team
                            serverUser.myTeams.add(team);   //Add the team to user
                            team.setAdmin(serverUser);      //Make him an admin, because he created the team
                            Server.teams.add(team);         //Finally add the team to the server
                            sendSTRequestToClient("createTeam:" + team);
                        }
                        case "getTeams" -> {
                            System.out.println("args = " + Arrays.toString(args));
                            User serverUser = Server.users.get(Server.users.indexOf(extractUserFromArgs(args)));
                            StringBuilder request = new StringBuilder();
                            for (Team team : Server.teams) {
                                if (team.members.contains(serverUser)) {
                                    request.append(team).append(";");
                                }
                            }
                            String clientRequest = request.toString();
                            try {
                                sendSTRequestToClient("userTeams:" + clientRequest.substring(0, clientRequest.length() - 1));
                            } catch (StringIndexOutOfBoundsException ignored) {
                            }
                        }
                        case "registerUser" -> {    // registerUser:email='email',username='username',password='password',name='name',lastname='lastname',birth='age'
                            System.out.println("args = " + Arrays.toString(args));
                            User user = extractUserFromArgs(args);
                            if (Server.users.contains(user)) {
                                sendSTRequestToClient("userExists");
                            } else {
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
                            System.out.println("args = " + Arrays.toString(args));
                            User user = BasicFunctionLibrary.extractUserFromArgs(args);
                            Message message = new Message(user, BasicFunctionLibrary.findValueFromArgs("messageText", args), Message.dateFormat.parse(BasicFunctionLibrary.findValueFromArgs("date", args)));
                            Team team = new Team(BasicFunctionLibrary.findValueFromArgs("teamname", args), BasicFunctionLibrary.findValueFromArgs("teamdesc", args));
                            Server.teams.get(Server.teams.indexOf(team)).getChatroom().addMessage(message);
                        }
                        case "addUserToTeam" -> {
                            User invitedUser = BasicFunctionLibrary.extractUserFromArgs(args);
                            Team team = new Team(BasicFunctionLibrary.findValueFromArgs("teamname", args), BasicFunctionLibrary.findValueFromArgs("teamdesc", args));
                            Server.teams.get(Server.teams.indexOf(team)).members.add(Server.users.get(Server.users.indexOf(invitedUser)));
                            Server.listeners.get(invitedUser).sendSTRequestToClient("requestTeams");
                        }
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


    public Listener(Socket s) {
        socket = s;
    }

    public boolean sendSTRequestToClient(String message) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(message);
            dataOutputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
