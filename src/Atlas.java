import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Atlas extends Frame {
    Panel northPanel,centerPanel,westPanel,southPanel;
    Label lbl1, lbl2, lbl3, lbl4;
    Button addButton,removeButton,updateButton,confirmButton,firstButton,lastButton,nextButton,backButton;
    TextField input1,input2,input3,input4;

    static int index = 0;
    static Pays currentPays;
    String mode ;

    public void updatePaysDetails() {
        input1.setText(currentPays.getNom());
        input2.setText(currentPays.getCapitale());
        input3.setText(Integer.toString(currentPays.getPopulution()));
        input4.setText(currentPays.getContinent());
    }

    public void toggleInputs(boolean state) {
        input1.setEditable(state);
        input2.setEditable(state);
        input3.setEditable(state);
        input4.setEditable(state);
    }

    public void toggleButtons(boolean state){
        addButton.setEnabled(state);
        updateButton.setEnabled(state);
        removeButton.setEnabled(state);
        firstButton.setEnabled(state);
        lastButton.setEnabled(state);
        nextButton.setEnabled(state);
        backButton.setEnabled(state);
        confirmButton.setEnabled(!state);
    }

    public void showErrorDialog(Frame parent, String message) {
        Dialog dialog = new Dialog(parent, "Input Error", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 150);

        Label errorLabel = new Label(message, Label.CENTER);
        dialog.add(errorLabel, BorderLayout.CENTER);

        Button okButton = new Button("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });
        dialog.add(okButton, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public Pays createPays(){
        try{
            Pays p = new Pays();
            p.setNom(input1.getText());
            p.setCapitale(input2.getText());
            p.setPopulution(Integer.parseInt(input3.getText()));
            p.setContinent(input4.getText());
            return p;
        }catch (Exception e){
            showErrorDialog(Atlas.this, "Please enter a valid integer for population.");
            return null;
        }
    }

    public boolean showConfirmDialog(Frame parent, String message, String title) {
        final boolean[] res = {false};
        Dialog dialog = new Dialog(parent, title, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 150);

        Label lblMessage = new Label(message, Label.CENTER);
        dialog.add(lblMessage, BorderLayout.CENTER);

        Panel buttonPanel = new Panel();
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        yesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                res [0]= true;
                dialog.setVisible(false);
            }
        });

        noButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });

        dialog.setVisible(true);
        return res[0];
    }


    public Atlas(){
        super("Atlas");
        setSize(400,400);
        this.setLayout(new BorderLayout());

        PaysManagement management = new PaysManagement();
//        management.fetchPays("src/data.txt");
        management.fetchFromDatabase();
        currentPays = management.getOnePays(index);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                boolean confirmed = showConfirmDialog(Atlas.this,
                        "Are you sure you want to exit?", "Exit Confirmation");

                if (confirmed) {
//                    management.savePays("src/data.txt");
                    management.savePaysInDatabase();
                    System.exit(0);
                }
            }
        });

        northPanel = new Panel(new GridBagLayout());

        addButton = new Button("ajouter");
        addButton.setBackground(Color.BLUE);
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                input1.setText("");
                input2.setText("");
                input3.setText("");
                input4.setText("");
                toggleButtons(false);
                toggleInputs(true);
                mode = "add";
            }
        });
        addButton.setPreferredSize(new Dimension(100, 30));
        northPanel.add(addButton);

        updateButton = new Button("modifier");
        updateButton.setBackground(Color.YELLOW);
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {;
                addButton.setEnabled(false);
                removeButton.setEnabled(false);
                confirmButton.setEnabled(true);
                toggleInputs(true);
                mode = "update";
            }
        });
        updateButton.setPreferredSize(new Dimension(100, 30));
        northPanel.add(updateButton);

        confirmButton = new Button("valider");
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Pays p = createPays();
                if (p != null){
                    if (mode.equals("add")){
                        management.addPays(p);
                    }else if(mode.equals("update")){
                        management.updatePays(Atlas.index,p);
                    }
                }else{
                    Atlas.index = 0;
                    updatePaysDetails();
                }
                mode = null;
                toggleButtons(true);
                toggleInputs(false);
            }
        });
        confirmButton.setPreferredSize(new Dimension(100, 30));
        northPanel.add(confirmButton);

        removeButton = new Button("supprimer");
        removeButton.setBackground(Color.RED);
        removeButton.setForeground(Color.WHITE);
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                boolean confirmed = showConfirmDialog(Atlas.this,
                        "Are you sure you want to delete this country?", "Delete Confirmation");
                if (confirmed) {
                    management.removePays(index);
                    Atlas.index = 0;
                }
            }
        });
        removeButton.setPreferredSize(new Dimension(100, 30));
        northPanel.add(removeButton);

        this.add(northPanel,BorderLayout.NORTH);

        westPanel = new Panel(new GridLayout(4,1));

        lbl1 = new Label("Nom");
        lbl1.setPreferredSize(new Dimension(100, 30));
        westPanel.add(lbl1);

        lbl2 = new Label("Capitale");
        lbl2.setPreferredSize(new Dimension(100, 30));
        westPanel.add(lbl2);

        lbl3 = new Label("Population");
        lbl3.setPreferredSize(new Dimension(100, 30));
        westPanel.add(lbl3);

        lbl4 = new Label("Continent");
        lbl4.setPreferredSize(new Dimension(100, 30));
        westPanel.add(lbl4);

        this.add(westPanel,BorderLayout.WEST);

        centerPanel = new Panel(new GridLayout(4,1));

        input1 = new TextField(currentPays != null ? currentPays.getNom() : "",10);
        input1.setSize(400,180);
        centerPanel.add(input1);

        input2 = new TextField(currentPays != null ? currentPays.getCapitale() : "",10);
        centerPanel.add(input2);

        input3 = new TextField(currentPays != null ? Integer.toString(currentPays.getPopulution()):"",10);
        centerPanel.add(input3);

        input4 = new TextField(currentPays != null ?currentPays.getContinent() : "",10);
        centerPanel.add(input4);
        toggleInputs(false);

        this.add(centerPanel,BorderLayout.CENTER);

        southPanel = new Panel(new GridBagLayout());

        firstButton = new Button("|<");
        firstButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Atlas.index = 0;
                currentPays = management.getOnePays(index);
                updatePaysDetails();
            }
        });
        firstButton.setPreferredSize(new Dimension(100, 30));
        southPanel.add(firstButton);

        backButton = new Button("<<");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (Atlas.index > 0) Atlas.index--;
                currentPays = management.getOnePays(index);
                updatePaysDetails();
            }
        });
        backButton.setPreferredSize(new Dimension(100, 30));
        southPanel.add(backButton);

        nextButton = new Button(">>");
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (!(Atlas.index >= management.getPays().size()-1)) Atlas.index++;
                currentPays = management.getOnePays(index);
                updatePaysDetails();
            }
        });
        nextButton.setPreferredSize(new Dimension(100, 30));
        southPanel.add(nextButton);

        lastButton = new Button(">|");
        lastButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Atlas.index = management.getPays().size() - 1;
                currentPays = management.getOnePays(index);
                updatePaysDetails();
            }
        });
        lastButton.setPreferredSize(new Dimension(100, 30));
        southPanel.add(lastButton);

        this.add(southPanel,BorderLayout.SOUTH);

        this.setVisible(true);
    }
    
}
