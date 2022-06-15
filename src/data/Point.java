package data;


import java.io.*;

public class Point{
    //Lưu điểm cao vào mức chơi k
    public void checkPoint(String s, int k) throws IOException {
        String file = "src\\point.txt";
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String[] str = new String[]{"", "", ""};
        for(int i = 0; i <= 2; ++i) {
            str[i] = br.readLine();
        }
        fr.close();
        if (s.compareTo(str[k]) < 0) {
            str[k] = s;
            FileWriter f = new FileWriter(file);
            f.write(str[0] + "\n");
            f.write(str[1] + "\n");
            f.write(str[2] + "\n");
            f.flush();
            f.close();
        }

    }
}
