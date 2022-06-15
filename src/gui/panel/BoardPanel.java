package gui.panel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import gui.ICommon;
import gui.ITrans;
import logic.Board;
import logic.Square;

public class BoardPanel extends JPanel implements ICommon {
    private static final long serialVersionUID = -6403941308246651773L;
    private Label[][] lbSquare;     //Mảng kiểu Label cài đặt hiển thị cho từng ô vuông trên bàn chơi
    private ITrans listener;        //listener có thể gọi các phương thức đã ghi đè ở Gui
    private int numSquareClosed;    //Số ô chưa mở
    private Board board;            // đối tượng board và chỉ số hàng và cột
    public int r ;
    public int c ;

    public BoardPanel(int k){
        board = new Board(k);
        this.r = board.getNUM_ROWS();
        this.c = board.getNUM_COLUMNS();
        initComp();
        addComp();
        addEvent();
    }
    //Tạo lưới các ô vuông theo số hàng và cột của board và không có khoảng cách giữa các ô
    @Override
    public void initComp() {
        setLayout(new GridLayout(r,c));
    }
    //Khởi tạo và cài đặt hiển thị cho từng ô vuông
    @Override
    public void addComp() {
        Border border = BorderFactory.createLineBorder(Color.gray, 1);
        lbSquare = new Label[r][c];
        for (int i = 0; i < lbSquare.length; i++) {
            for (int j = 0; j < lbSquare[0].length; j++) {
                lbSquare[i][j] = new Label();
                lbSquare[i][j].setOpaque(true);
                //Do mặc định màu nền của Label là trong suốt nên để setOpaque(true) thì setBackground mới có hiệu lực
                lbSquare[i][j].setBackground(new Color(242, 242, 242));
                lbSquare[i][j].setBorder(border);
                lbSquare[i][j].setHorizontalAlignment(JLabel.CENTER);    //căn text và giữa Label theo hàng ngang
                lbSquare[i][j].setVerticalAlignment(JLabel.CENTER);      //căn text và giữa Label theo hàng dọc
                add(lbSquare[i][j]);
            }
        }
    }

    //Sự kiên click chuột trái là mở ô vuông còn chuột phải là đánh dấu
    @Override
    public void addEvent() {
        for (int i = 0; i < lbSquare.length; i++) {
            for (int j = 0; j < lbSquare[0].length; j++) {
                lbSquare[i][j].x = i;
                lbSquare[i][j].y = j;
                lbSquare[i][j].addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            Label label = (Label) e.getComponent();
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            try {
                                listener.play(label.x, label.y);
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            listener.target(label.x, label.y);
                        }
                        }
                });
            }
        }
    }

    public void addListener(ITrans event) {
        listener = event;
    }

    // cập nhật hiển thị của bàn chơi
    public void updateBoard() {
        Font font = new Font("VNI", Font.PLAIN, 20);
        numSquareClosed = 0;
        Square[][] listSquare = listener.getListSquare();
        for (int i = 0; i < listSquare.length; i++) {
            for (int j = 0; j < listSquare[0].length; j++) {
                lbSquare[i][j].setFont(font);
                if (!listSquare[i][j].isOpen()) {
                    lbSquare[i][j].setBackground(new Color(242, 242, 242));
                    lbSquare[i][j].setForeground(Color.black);
                    numSquareClosed++;
                    if (!listSquare[i][j].isTarget()) {
                        lbSquare[i][j].setText("");
                    } else {
                        lbSquare[i][j].setText("\uD83D\uDEA9"); // ki tu 'flag'
                    }
                } else {
                    if (listSquare[i][j].isHasMine()) {
                        lbSquare[i][j].setText("\uD83D\uDCA3"); // ki tu 'bomb'
                    } else {
                        int numMineAround = listSquare[i][j].getNumMineAround();
                        if (numMineAround == 0) {
                            lbSquare[i][j].setText("");
                        } else {
                            lbSquare[i][j].setText(numMineAround + "");
                            switch (numMineAround) {
                                case 1:
                                    lbSquare[i][j].setForeground(new Color(128, 128, 128));
                                    break;
                                case 2:
                                    lbSquare[i][j].setForeground(new Color(255, 0, 0));
                                    break;
                                case 3:
                                    lbSquare[i][j].setForeground(new Color(0, 204, 0));
                                    break;
                                case 4:
                                    lbSquare[i][j].setForeground(new Color(102, 0, 255));
                                    break;
                                case 5:
                                    lbSquare[i][j].setForeground(new Color(128, 128, 128));
                                    break;
                                case 6:
                                    lbSquare[i][j].setForeground(new Color(255, 0, 0));
                                    break;
                                case 7:
                                    lbSquare[i][j].setForeground(new Color(0, 204, 0));
                                    break;
                                case 8:
                                    lbSquare[i][j].setForeground(new Color(102, 0, 255));
                                    break;
                            }
                        }
                    }
                    lbSquare[i][j].setBackground(Color.white);
                }
            }
        }
    }

    //Tạo class con để mở rộng thuộc tính cho lớp JLabel
    //giúp lưu được chỉ số hàng, cột của label đó ở trong GridLayout
    //vì ko thể truyền giá trị i, j vào bên trong phương thức addMouseListener
    private class Label extends JLabel {
        private static final long serialVersionUID = 6099893043079770073L;
        private int x;
        private int y;
    }

    public int getNumSquareClosed() {
        return numSquareClosed;
    }
}