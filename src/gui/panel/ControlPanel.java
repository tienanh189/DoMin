package gui.panel;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import gui.ICommon;
import gui.ITrans;
import logic.Board;
import data.Point;

public class ControlPanel extends JPanel implements ICommon {
    private static final long serialVersionUID = 5162710183389028792L;
    private JLabel lbNumSquareClosed;
    private JLabel lbTime;
    private JLabel lbLevel;
    private JComboBox lv;
    private JButton btnRestart;
    private ITrans listener;
    private Timer timer;
    private int k = 0;
    private Board board;
    private Point point;
    

    public ControlPanel() {
        point = new Point();
        board = new Board(k);
        initComp();
        addComp();
        addEvent();
    }

    @Override
    public void initComp() {
        setLayout(null);
    }

    @Override
    public void addComp() {
        Font font = new Font("VNI", Font.PLAIN, 20);

        lbNumSquareClosed = new JLabel();
        lbNumSquareClosed.setFont(font);
        lbNumSquareClosed.setText("Số ô chưa mở: " + board.getNUM_ROWS()*board.getNUM_COLUMNS() );
        lbNumSquareClosed.setBounds(10, 10, 230, 40);
        add(lbNumSquareClosed);

        lbLevel= new JLabel("Mức Độ: ");
        lbLevel.setFont(font);
        lbLevel.setBounds(200,10,100,40);
        add(lbLevel);

        lv = new JComboBox();
        lv.setFont(new Font("Arial",0, 15));
        lv.setBounds(290,10,110,40);
        this.lv.addItem("Dễ");
        this.lv.addItem("Trung bình");
        this.lv.addItem("Khó");
        lv.setSelectedIndex(k);
        add(lv);

        lbTime = new JLabel("00:00:00:00");
        lbTime.setFont(font);
        lbTime.setBounds(420, 10, 150, 40);
        add(lbTime);

        btnRestart = new JButton();
        btnRestart.setFont(font);
        btnRestart.setText("Chơi lại");
        btnRestart.setBounds(550, 10, 150, 40);
        add(btnRestart);
    }

    @Override
    public void addEvent() {
        this.timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lbTime.setText(next(lbTime));
            }
        });
        btnRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lv.getSelectedItem()=="Dễ"){
                    k=0;
                }
                if (lv.getSelectedItem()=="Trung bình"){
                    k=1;
                }
                if (lv.getSelectedItem()=="Khó"){
                    k=2;
                }
                listener.restart();
                board = new Board(k);
                timeStop();
                lbNumSquareClosed.setText("Số ô chưa mở: " + board.getNUM_ROWS()*board.getNUM_COLUMNS());
                lbTime.setText("00:00:00:00");
            }
        });
    }

    public void addListener(ITrans event) {
        listener = event;
    }

    public void updateStatus(int numSquareClosed) throws IOException {
        lbNumSquareClosed.setText("Số ô chưa mở: " + numSquareClosed);
        if (numSquareClosed <= board.getNUM_ROWS()*board.getNUM_COLUMNS()){
            timer.start();
        }
        if (numSquareClosed == board.getNUM_MINES()) {
            String s = lbTime.getText();
            this.timer.stop();
            JOptionPane.showMessageDialog(null,"Ban đã Thắng");
            try {
                point.checkPoint(s,getK());
            } catch (IOException var4) {
                var4.printStackTrace();
            }
        }else if(numSquareClosed ==0){
            this.timer.stop();
            JOptionPane.showMessageDialog(null,"Ban đã Thua");
        }
    }

    //Hàm cập nhật time, 100 mili = 1s
    public String next(JLabel lb){
        String[] str = lb.getText().split(":");
        int tt = Integer.parseInt(str[3]);
        int s = Integer.parseInt(str[2]);
        int m = Integer.parseInt(str[1]);
        int h = Integer.parseInt(str[0]);
        String kq = "";
        int sum = tt + s * 100 + m * 60 * 100 + h * 60 * 60 * 100 + 1;
        if (sum % 100 > 9) {
            kq = ":" + sum % 100 + kq;
        } else {
            kq = ":0" + sum % 100 + kq;
        }
        sum /= 100;
        if (sum % 60 > 9) {
            kq = ":" + sum % 60 + kq;
        } else {
            kq = ":0" + sum % 60 + kq;
        }
        sum /= 60;
        if (sum % 60 > 9) {
            kq = ":" + sum % 60 + kq;
        } else {
            kq = ":0" + sum % 60 + kq;
        }
        sum /= 60;
        if (sum > 9) {
            kq = sum + kq;
        } else {
            kq = "0" + sum + kq;
        }
        return kq;
    }


    public void timeStop(){
        timer.stop();
    }

    public int getK(){
        return k;
    }
}
