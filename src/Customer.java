import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Customer extends Frame implements Runnable, ActionListener {
    TextField textField;
    TextArea textArea;
    Button send;

    // for data transfering need network

    Socket socket;

    //Data transfering
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;


    Thread chat;
    Customer(){
        textField = new TextField();
        textArea = new TextArea();
        send = new Button("Send");

        send.addActionListener(this);
//in Connection stablishing time sometime we'll get error, so for handling this error using trycatch
        try {
            socket = new Socket("localhost", 12000); // accepting request from client side and storing in socket


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
        setTitle("Customer");
        setLayout(new FlowLayout());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = textField.getText();
        textArea.append("Customer:"+msg+"\n"); // in here im sending msg so add seller
        textField.setText(""); // after sending msg textbox want to change as empty

        try {
            dataOutputStream.writeUTF(msg); // sending msg
            dataOutputStream.flush(); // using flush for sending quickly, not delaying in buffer

        } catch (IOException ex) {

        }
    }

    public static void main(String[] args) {
        new Customer();
    }
    public void run(){
        while (true){
            try {
                String msg = dataInputStream.readUTF();
                textArea.append("Seller:"+msg+"\n");
            }
            catch (Exception E){

            }
        }
    }
}
