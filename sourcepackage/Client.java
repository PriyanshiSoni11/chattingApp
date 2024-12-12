package sourcepackage;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client implements ActionListener {
    JTextField text1;
    JScrollPane scrollPane;
    static JPanel a2;
    static DataOutputStream dout;
    static Box clientVertical = Box.createVerticalBox();
    static JFrame clientFrame = new JFrame();
    Client(){
        clientFrame.setLayout(null);

        JPanel p1 = new JPanel();
        p1.setBackground(new Color(7,94,84));
        p1.setBounds(0,0,450,70);
        p1.setLayout(null);
        clientFrame.add(p1);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/3.png"));
        Image i2 = i1.getImage().getScaledInstance(25,25, Image.SCALE_DEFAULT);
        JLabel back = new JLabel(new ImageIcon(i2));
        back.setBounds(5,20,25,25);
        p1.add(back);
        back.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("icons/bunty.jpeg"));
        Image i4 = i3.getImage().getScaledInstance(50,50, Image.SCALE_DEFAULT);
        JLabel profile = new JLabel(new ImageIcon(i4));
        profile.setBounds(40,10,50,50);
        p1.add(profile);

        ImageIcon i5 = new ImageIcon(ClassLoader.getSystemResource("icons/video.png"));
        Image i6 = i5.getImage().getScaledInstance(30,30, Image.SCALE_DEFAULT);
        JLabel video = new JLabel(new ImageIcon(i6));
        video.setBounds(300,10,50,50);
        p1.add(video);

        ImageIcon i7 = new ImageIcon(ClassLoader.getSystemResource("icons/phone.png"));
        Image i8 = i7.getImage().getScaledInstance(35,30, Image.SCALE_DEFAULT);
        JLabel call = new JLabel(new ImageIcon(i8));
        call.setBounds(360,20,35,30);
        p1.add(call);

        ImageIcon i9 = new ImageIcon(ClassLoader.getSystemResource("icons/3icon.png"));
        Image i10 = i9.getImage().getScaledInstance(10,25, Image.SCALE_DEFAULT);
        JLabel more = new JLabel(new ImageIcon(i10));
        more.setBounds(420,20,10,25);
        p1.add(more);

        JLabel name = new JLabel("Bunty");
        name.setBounds(110, 15, 100, 18);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("SAN_SARIF", Font.BOLD, 18));
        p1.add(name);

        JLabel status = new JLabel("Active now");
        status.setBounds(110, 35, 100, 18);
        status.setForeground(Color.WHITE);
        status.setFont(new Font("SAN_SARIF", Font.ITALIC, 12));
        p1.add(status);

        a2 = new JPanel();
        a2.setLayout(new BoxLayout(a2, BoxLayout.Y_AXIS)); // Use BoxLayout for stacking messages vertically.
        scrollPane = new JScrollPane(a2);
        scrollPane.setBounds(5, 75, 440, 570);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        clientFrame.add(scrollPane);


        text1 = new JTextField();
        text1.setBounds(5,655,310,40);
        status.setFont(new Font("SAN_SARIF", Font.PLAIN, 12));
        clientFrame.add(text1);

        JButton sendButton = new JButton("Send");
        sendButton.setBounds(320,655, 123, 40);
        sendButton.setBackground(new Color(7,94,84));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("SAN_SARIF", Font.PLAIN, 16));
        sendButton.setOpaque(true);
        sendButton.setBorderPainted(false);
        sendButton.addActionListener(this);
        clientFrame.add(sendButton);

        clientFrame.setSize(450, 700);
        clientFrame.setLocation(800,50);
        clientFrame.setUndecorated(true);
        clientFrame.getContentPane().setBackground(Color.WHITE);

        clientFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String out = text1.getText();
            JPanel p2 = formatLabel2(out, new Color(150, 200, 190));
            a2.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(p2, BorderLayout.LINE_END);
            clientVertical.add((right));
            clientVertical.add(Box.createVerticalStrut(15));
            a2.add(clientVertical, BorderLayout.PAGE_START);
            scrollToBottom2(scrollPane);

            dout.writeUTF(out);
            text1.setText("");

            clientFrame.repaint();
            clientFrame.invalidate();
            clientFrame.validate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static JPanel formatLabel2(String out, Color color){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style=\"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16));
        output.setBackground(color);
        output.setOpaque(true);
        output.setBorder(new EmptyBorder(15,15,15,50));
        panel.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel("12:00");
        time.setText(sdf.format(cal.getTime()));
        panel.add(time);


        return panel;
    }

    private void scrollToBottom2(JScrollPane scrollPane) {
        SwingUtilities.invokeLater(() -> {
            JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
            verticalBar.setValue(verticalBar.getMaximum());
        });
    }


    public static void main(String[] args) {
        new Client();
        try{
                Socket s = new Socket("127.0.0.1", 6001);
                DataInputStream din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());

                while (true){
                    a2.setLayout(new BorderLayout());
                    String msg = din.readUTF();
                    JPanel panel = formatLabel2(msg, Color.WHITE);

                    JPanel left = new JPanel(new BorderLayout());
                    left.add(panel, BorderLayout.LINE_START);
                    clientVertical.add(left);

                    clientVertical.add(Box.createVerticalStrut(15));
                    a2.add(clientVertical,BorderLayout.PAGE_START);
                    clientFrame.validate();

                }
        } catch (Exception e){
            e.printStackTrace();

        }
    }

}
