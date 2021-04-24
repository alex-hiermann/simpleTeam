package Server;

import Client.User;
import Client.Team;
import Utils.BasicFunctionLibrary;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.LinkedList;

import static Utils.BasicFunctionLibrary.extractUserFromArgs;

public class Listener implements Runnable {

    static Socket socket;

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String data = "";

            //STRequest handling
            synchronized (this) {
                try {
                    while (!(data = dataInputStream.readUTF()).isEmpty()) {
                        String command = data.split(":")[0];
                        String args[] = new String[0];
                        try {
                            args = data.split(":")[1].split(",");
                        } catch (Exception ignored) {
                        }
                        switch (command) {
                            case "createTeam" -> {
                                Team team = new Team(BasicFunctionLibrary.findValueFromArgs("name", args), BasicFunctionLibrary.findValueFromArgs("desc", args));
                                User serverUser = Server.users.get(Server.users.indexOf(extractUserFromArgs(args)));
                                team.members.add(serverUser);   //Add user to team
                                serverUser.myTeams.add(team);   //Add the team to user
                                team.setAdmin(serverUser);      //Make him an admin, because he created the team
                                Server.teams.add(team);         //Finally add the team to the server
                                sendSTRequestToClient("createTeam:" + team);
                            }
                            case "getTeams" -> {
                                User serverUser = Server.users.get(Server.users.indexOf(extractUserFromArgs(args)));
                                StringBuilder request = new StringBuilder();
                                for (Team team : Server.teams) {
                                    if (team.members.contains(serverUser)) {
                                        request.append(team).append(";");
                                    }
                                }
                                sendSTRequestToClient("userTeams:" + request.substring(0, request.toString().length() - 1));
                            }
                            case "registerUser" -> {    // registerUser:email='email',username='username',password='password',name='name',lastname='lastname',birth='age'
                                User user = extractUserFromArgs(args);
                                if (Server.users.contains(user)) {
                                    sendSTRequestToClient("userExists");
                                } else {
                                    Server.users.add(user);
                                    sendSTRequestToClient("userRegistered");
                                }
                            }
                            case "login" -> {
                                User user = new User(
                                        BasicFunctionLibrary.findValueFromArgs("email", args),
                                        BasicFunctionLibrary.findValueFromArgs("password", args));
                                try {
                                    User foundUser = Server.users.get(Server.users.indexOf(user));
                                    if (foundUser.getEmail().equalsIgnoreCase(user.getEmail()) && foundUser.getPassword().equals(user.getPassword())) {
                                        System.err.println(foundUser);
                                        sendSTRequestToClient("canLogin:" + foundUser);
                                    } else {
                                        sendSTRequestToClient("rejectedLogin");
                                    }
                                } catch (Exception ignored) {
                                    sendSTRequestToClient("rejectedLogin");
                                }
                            }
                        }
                    }
                } catch (SocketException ignored) {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public Listener(Socket s) {
        socket = s;
    }

    public static boolean sendSTRequestToClient(String message) {
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
