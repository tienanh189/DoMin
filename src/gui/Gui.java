package gui;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import gui.panel.BoardPanel;
import gui.panel.ControlPanel;
import logic.Board;
import logic.Square;
public class Gui extends JFrame implements ICommon, ITrans {
    private static final long serialVersionUID = -5479701518838741039L;
    private static final String TITLE = "MineSweeper";  //Tiêu đề game
    public static final int FRAME_WIDTH = 730;          //Kích thước mặc định của cửa sổ game khi bảng thay đổi
    public static final int FRAME_HEIGHT = 600;         // thì chỉ thay đổi kích cỡ ô vuông chứ không thay đổi kích thước cửa sổ
    private BoardPanel boardPanel;                      //Các đối tượng Panel
    private ControlPanel controlPanel;
    private Board board;                                // Đối tượng board
    private int k ;

    public Gui() {      //Hàm tạo Gui
        board = new Board(k);
        initComp();
        addComp();
        addEvent();
    }
    @Override
    public void initComp() {
        setTitle(TITLE);                                //Tiêu đề
        setSize(FRAME_WIDTH, FRAME_HEIGHT);             //Kích thước mặc định của cửa sổ
        setLocationRelativeTo(null);                    //Đặt cửa sổ ra chính giữa màn hình
        setResizable(false);                            //Không cho phép kéo thả màn hình
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);  //Không làm gì khi click vào nút 'X"
        setLayout(null);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Thêm boardPanel và controlPanel vào Frame
    @Override
    public void addComp() {
        boardPanel = new BoardPanel(k);
        boardPanel.setBounds(10, 60, 705, 500);
        add(boardPanel);
        boardPanel.addListener(this);
        //"This" chính là đại diện cho Gui
        controlPanel = new ControlPanel();
        controlPanel.setBounds(10, 0, 705, 60);
        add(controlPanel);
        controlPanel.addListener(this);
    }
    //Khi click vào dấu ích sẽ hiện ra một cửa sổ mới cho bạn lựu chọn thoát game hoặc không
    @Override
    public void addEvent() {
        WindowListener wd = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int kq = JOptionPane.showConfirmDialog(Gui.this, "Bạn có muốn thoát không?",
                        "Thông báo", JOptionPane.YES_NO_OPTION);
                if (kq == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        };
        addWindowListener(wd);
    }

    @Override
    public Square[][] getListSquare() {
        return board.getListSquare();
    }

    @Override
    public void play(int x, int y) throws IOException {
        boolean check = board.play(x, y);
        if (!check) {
            board.showAllSquares();
        }
        boardPanel.updateBoard();
        int numSquareClosed = boardPanel.getNumSquareClosed();
        controlPanel.updateStatus(numSquareClosed);
    }

    @Override
    public void target(int x, int y) {
        board.target(x, y);
        boardPanel.updateBoard();
    }
    //Phương thức restart ẩn đi bàn chơi cũ và câp nhật bàn chơi mới
    @Override
    public void restart() {
        this.boardPanel.setVisible(false);
        boardPanel = new BoardPanel(controlPanel.getK());
        boardPanel.setBounds(10, 60, 705, 500);
        add(boardPanel);
        boardPanel.addListener(this);
        board = new Board(controlPanel.getK());
        boardPanel.updateBoard();
    }
}
