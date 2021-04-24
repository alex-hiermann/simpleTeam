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
                        String args[] = data.split(":")[1].split(",");
                        switch (command) {
                            case "createTeam" -> {
                                Team team = new Team(BasicFunctionLibrary.findValueFromArgs("name", args), BasicFunctionLibrary.findValueFromArgs("desc", args));
                                Server.teams.add(team);
                                sendSTRequestToClient("createTeam:" + team);
                            }
                            case "getTeams" -> {
                                String request = "";
                                for (Team team : Server.teams) {
                                }
                            }
                            case "registerUser" -> {    // registerUser:email='email',username='username',password='password',name='name',lastname='lastname',birth='age'
                                User user = new User(
                                        BasicFunctionLibrary.findValueFromArgs("username", args),
                                        BasicFunctionLibrary.findValueFromArgs("name", args),
                                        BasicFunctionLibrary.findValueFromArgs("lastname", args),
                                        BasicFunctionLibrary.findValueFromArgs("email", args),
                                        new Date(BasicFunctionLibrary.findValueFromArgs("birth", args)),
                                        BasicFunctionLibrary.findValueFromArgs("password", args));
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
