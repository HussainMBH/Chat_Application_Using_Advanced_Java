import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Seller extends Frame implements Runnable, ActionListener {
    TextField textField;
    TextArea textArea;
    Button send;

    // for data transfering need network
    ServerSocket serverSocket;
    Socket socket;

    //Data transfering
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;


    Thread chat;
    Seller(){
        textField = new TextField();
        textArea = new TextArea();
        send = new Button("Send");

        send.addActionListener(this);
//in Connection stablishing time sometime we'll get error, so for handling this error using trycatch
        try {
            socket = new Socket("localhost", 12000); // accepting request from client side and storing in socket
            serverSocket = new ServerSocket(12000); //it's only sometime will come error
            socket = serverSocket.accept(); // accepting request from client side and storing in socket


            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

        }
        catch (Exception E){

        }
        add(textField);
        add(textArea);
        add(send);


        chat = new Thread(this);
        chat.setDaemon(true); //it's for setting higher priorities
        chat.start();

        setSize(500,500);
        setTitle("Seller");
        setLayout(new FlowLayout());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = textField.getText();
        textArea.append("Seller:"+msg+"\n"); // in here im sending msg so add seller
        textField.setText(""); // after sending msg textbox want to change as empty

        try {
            dataOutputStream.writeUTF(msg); // sending msg
            dataOutputStream.flush(); // using flush for sending quickly, not delaying in buffer

        } catch (IOException ex) {

        }
    }

    public static void main(String[] args) {
        new Seller();
    }
    public void run(){
        while (true){
            try {
                String msg = dataInputStream.readUTF();
                textArea.append("Customer:"+msg+"\n");
            }
            catch (Exception E){

            }
        }
    }
}
