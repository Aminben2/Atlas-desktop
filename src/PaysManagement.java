import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaysManagement {
    private List<Pays> pays = new ArrayList<Pays>();

    public List<Pays> getPays() {
        return pays;
    }

    public void setPays(List<Pays> pays) {
        this.pays = pays;
    }

    public void addPays(Pays pays){
        this.pays.add(pays);
    }

    public void removePays(int index){
        this.pays.remove(index);
    }

    public void updatePays(int index, Pays pays){
        this.pays.set(index,pays);
    }

    public Pays getOnePays(int index){
        if (this.pays == null || this.pays.isEmpty()) {
            return null;
        }
        return this.pays.get(index);
    }

    public void fetchPays(String pathFile) {
        try{
            FileReader database = new FileReader(pathFile);
            BufferedReader bufferedReader = new BufferedReader(database);

            List<Pays> countryList = new ArrayList<Pays>();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                Pays country = new Pays();
                String data[] = line.split(";");
                country.setNom(data[0]);
                country.setCapitale(data[1]);
                country.setPopulution(Integer.parseInt(data[2]));
                country.setContinent(data[3]);
                countryList.add(country);
            }
            database.close();
            this.setPays(countryList);
        }catch (IOException exception){
            System.out.println("Something went wrong in data fetching : "+exception.getMessage());
        }
    }
    
    public void savePays(String pathFile){
        try{
            PrintWriter file = new PrintWriter(pathFile);
            for (Pays pays : this.pays){
                file.println(pays);
            }
            file.close();
        }catch (IOException exception){
            System.out.println("Something went wrong in data saving : "+exception.getMessage());
        }
    }

    public Connection createConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/atlas";
            String username = "username";
            String password = "password";
            return DriverManager.getConnection(url,username,password);
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
  }

    public void fetchFromDatabase(){
        try{
            Connection connection = createConnection();
            Statement st = connection.createStatement();
            ResultSet data = st.executeQuery("select * from pays");

            List<Pays> countryList = new ArrayList<Pays>();
            while (data.next()){
                Pays pays = new Pays();
                pays.setNom(data.getString("nom"));
                pays.setCapitale(data.getString("capitale"));
                pays.setPopulution(data.getInt("pupilation"));
                pays.setContinent(data.getString("cantinent"));
                countryList.add(pays);
            }
            this.pays = countryList;
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public void savePaysInDatabase(){
        try{
            Connection connection = createConnection();
            PreparedStatement st = connection.prepareStatement("INSERT INTO pays (nom, capitale, pupilation, cantinent) VALUES (?, ?, ?, ?)");
            Statement stt = connection.createStatement();
            stt.executeUpdate("delete from pays");
            for (Pays pays : this.pays){
                st.setString(1, pays.getNom());
                st.setString(2, pays.getContinent());
                st.setInt(3, pays.getPopulution());
                st.setString(4, pays.getContinent());
                st.executeUpdate();
            }
        }catch (Exception  ex){
            System.out.println(ex.getMessage());
        }
    }
}
