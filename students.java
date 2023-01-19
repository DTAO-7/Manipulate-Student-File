


import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class STD {

    String studentFilePath;
    RandomAccessFile randA;

    byte[] Sid = new byte[4];
    byte[] Sname = new byte[30];
    byte[] Scgpa = new byte[4];
    byte[] Sdate = new byte[10];
    byte[] Sgender = new byte[1];
    byte[] SfullLine = new byte[55];

    String idString = null;
    String nameString = null;
    String cgpaString = null;
    String dateString = null;
    String genderString = null;
    String fullLineString = null;

    boolean Found = false;

    public HomeWork_ONE(String FilePath) throws EOFException, IOException {
        studentFilePath = FilePath;
    }

    public void modify(String stdid, String field, String new_value) throws EOFException, IOException {
        String wantedSTD = stdid;
        String wanterField = field;
        String change = new_value;
        boolean found = false;
        File student = new File(studentFilePath);
        randA = new RandomAccessFile(student, "rw");
        randA.seek(0);

        try {

            while (randA.getFilePointer() <= randA.length() - 2) {

                randA.read(Sid, 0, 4);
                idString = new String(Sid);
                int space;
                //System.out.println(idString);
                if (idString.equals(wantedSTD.trim())) {
                    //System.out.println("after if");
                    randA.seek(randA.getFilePointer() - 4);
                    switch (wanterField) {
                        
                        case "name":
                            space = 30 - change.length();
                            randA.seek(randA.getFilePointer() + 5);
                            randA.write(change.getBytes());
                            for (int i = space; i > 0; i--) {
                                randA.write(" ".getBytes());
                            }
                            break;
                        case "cgpa":
                            randA.seek(randA.getFilePointer() + 36);
                            randA.write(change.getBytes());
                            break;
                        case "date":
                            randA.seek(randA.getFilePointer() + 41);
                            randA.write(change.getBytes());
                            break;
                        case "gender":
                            randA.seek(randA.getFilePointer() + 52);
                            randA.write(change.getBytes());
                            break;
                        default:
                            System.out.println("Please Enter a valid field to change");
                            break;

                    }

                    found = true;
                }
                if (found) {
                    break;
                }
                randA.seek(randA.getFilePointer() - 4);
                //System.out.println(randA.getFilePointer());
                randA.seek(randA.getFilePointer() + 54);
                //randA.seek(0);

            }
            if (randA.getFilePointer() > randA.length() - 2) {
                System.out.println("Student not found");

            }
        } catch (EOFException e) {
        } catch (IOException e1) {
        }
        randA.close();
    }

    public void insert(String stdid, String name, String cgpa, String date, String gender) throws EOFException, IOException {
        File student = new File(studentFilePath);
        randA = new RandomAccessFile(student, "rw");
        randA.seek(randA.length());

        Sid = stdid.getBytes();
        Sname = name.getBytes();
        Scgpa = cgpa.getBytes();
        Sdate = date.getBytes();
        Sgender = gender.getBytes();

        try {
            if (randA.length() != 0) {
                randA.write(System.getProperty("line.separator").getBytes());
            }

            randA.write(Sid);
            randA.write(",".getBytes());

            randA.write(Sname);
            int a = Sname.length;
            for (int i = 30 - a; i > 0; i--) {
                randA.write(" ".getBytes());
            }
            randA.write(",".getBytes());
            randA.write(Scgpa);
            randA.write(",".getBytes());
            randA.write(Sdate);
            randA.write(",".getBytes());
            randA.write(Sgender);

        } catch (EOFException e) {
        } catch (IOException e1) {
        }
        randA.close();

    }

    public void delete(String stdid) throws EOFException, IOException {
        File student = new File(studentFilePath);
        RandomAccessFile randA = new RandomAccessFile(student, "rw");
        String wantedSTD = stdid;
        boolean found = false;
        String delLine = null;

        String data = "";
        try {
            while (randA.getFilePointer() <= randA.length() - 2) {
                randA.read(Sid, 0, 4);
                idString = new String(Sid);
                if (idString.equals(wantedSTD)) {
                    randA.seek(randA.getFilePointer() - 4);
                    randA.read(SfullLine, 0, 53);
                    delLine = new String(SfullLine);

                    found = true;
                    //System.out.println(delLine);
                }
                if (found) {
                    break;
                }
                randA.seek(randA.getFilePointer() + 51);
            }
        } catch (EOFException q) {
        } catch (IOException q1) {
        }

        if (found) {
            randA.seek(0);
            while ((fullLineString = randA.readLine()) != null) {
                if (fullLineString.trim().equals(delLine.trim())) {

                } else {

                    data += fullLineString + "\n";

                }
            }
        }
        randA.setLength(0);
        randA.write(data.getBytes());

        randA.close();
    }

    public void display() throws EOFException, IOException {
        File student = new File(studentFilePath);
        randA = new RandomAccessFile(student, "rw");
        String data;
        randA.seek(0);
        while ((data = randA.readLine()) != null) {
            System.out.println(data);
        }
        randA.close();
    }

    public void stats() throws EOFException, IOException {
        String data;
        String gender;
        String cgpa;
        float a = 0;
        int Fnum = 0;
        int Mnum = 0;
        int total = 0;
        float avg = 0;
        File student = new File(studentFilePath);
        randA = new RandomAccessFile(student, "rw");
        String filename = "\\staus";
        String path = student.getParent() + filename;
        File newpath = new File(path); //to write to
        RandomAccessFile randB = new RandomAccessFile(newpath, "rw");

        try {
            while (randA.getFilePointer() <= randA.length() - 2) {
                randA.read(SfullLine, 0, 53);
                fullLineString = new String(SfullLine);
                String[] fields = fullLineString.split(",");
                gender = fields[4].trim();

                switch (gender) {
                    case "M":
                        Mnum++;
                        break;
                    case "F":
                        Fnum++;
                        break;
                }
                cgpa = fields[2];
                a += Float.parseFloat(cgpa);
                randA.seek(randA.getFilePointer() + 2);
                total++;
            }
            avg = a / total;
            data = "Count of male students is: " + Mnum + "\nCount of female students is: " + Fnum
                    + "\nAverage cgpa is: " + avg + "\nTotal number of student is: " + total;
            try {
                randB.write(data.getBytes());

            } catch (EOFException e) {
            } catch (IOException e1) {
            }

        } catch (EOFException e) {
        } catch (IOException e1) {
        }
        randA.close();
        randB.close();

    }

    public static void main(String[] args) throws EOFException, IOException {
        String path = ("StudentF.txt");
        STD d = new STD(path);

        //d.delete("1234");
        //d.display();
        //d.insert("1234", "khan", "3.98", "05-04-2000", "M");
        //d.modify("6235", "gender", "M");
        //d.stats();
    }
}
