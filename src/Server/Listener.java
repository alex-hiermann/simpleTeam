package Server;

import Client.User;
import Client.Team;
import Utils.BasicFunctionLibrary;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

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
                                    //TODO
                                }
                            }
                            case "registerUser" -> {    // registerUser:email='email',username='username',password='password',name='name',lastname='lastname',age='age'
                                User user = new User(
                                        BasicFunctionLibrary.findValueFromArgs("username", args),
                                        BasicFunctionLibrary.findValueFromArgs("name", args),
                                        BasicFunctionLibrary.findValueFromArgs("lastname", args),
                                        BasicFunctionLibrary.findValueFromArgs("email", args),
                                        BasicFunctionLibrary.findValueFromArgs("age", args),
                                        BasicFunctionLibrary.findValueFromArgs("password", args));
                                Server.users.add(user);
                                sendSTRequestToClient("userRegistered");
                            }
                            case "logIn" -> {
                                User user = new User(
                                        BasicFunctionLibrary.findValueFromArgs("username", args),
                                        BasicFunctionLibrary.findValueFromArgs("name", args),
                                        BasicFunctionLibrary.findValueFromArgs("lastname", args),
                                        BasicFunctionLibrary.findValueFromArgs("email", args),
                                        BasicFunctionLibrary.findValueFromArgs("age", args),
                                        BasicFunctionLibrary.findValueFromArgs("password", args));
                                if (Server.users.contains(user)) {
                                    sendSTRequestToClient("canLogIn");
                                } else {
                                    sendSTRequestToClient("rejectedLogIn");
                                }
                            }
                        }
                    }
                } catch (SocketException sE) {

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
