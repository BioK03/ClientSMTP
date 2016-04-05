/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientsmtp;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bertrand
 */
public class Interface extends javax.swing.JFrame {
    
    private SSLSocket skt;
    private BufferedReader in;
    private PrintWriter out;
    private String utilisateur;
    private HashMap<Integer, String[]> messages;
    
    private int lastMessageSelected = 0;
    
    /**
     * Creates new form Interface
     * @param skt
     * @param in
     * @param out
     * @param nbMessages
     * @param utilisateur
     */
    public Interface(SSLSocket skt, BufferedReader in, PrintWriter out, int nbMessages, String utilisateur) {
        initComponents();
        this.messages = new HashMap();
        this.skt = skt;
        this.in = in;
        this.out = out;
        lb_user.setText("Bienvenue, "+utilisateur+"!");
        this.utilisateur = utilisateur;
        //this.gestionEvenement("LIST");
        creationLignesMessages(nbMessages);
        
        table_mails.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent event) {
                lastMessageSelected = Integer.parseInt(table_mails.getValueAt(table_mails.getSelectedRow(), 0).toString());
                if(table_mails.getSelectedColumn()<3)
                {
                    if(messages.containsKey(Integer.parseInt(table_mails.getValueAt(table_mails.getSelectedRow(), 0).toString())))
                    {
                        System.out.println("Message "+Integer.parseInt(table_mails.getValueAt(table_mails.getSelectedRow(), 0).toString())+" a été chargé depuis le cache");
                        table_mails.setValueAt(messages.get(Integer.parseInt(table_mails.getValueAt(table_mails.getSelectedRow(), 0).toString()))[0], lastMessageSelected-1, 2);
                        String line = messages.get(Integer.parseInt(table_mails.getValueAt(table_mails.getSelectedRow(), 0).toString()))[1];
                        table_mails.setValueAt(
                                /*line.substring(line.indexOf(" ")+1).substring(line.indexOf(" ")+1)*/line,
                                lastMessageSelected-1, 1);
                    }
                    else
                    {
                        //System.out.println("CLICKED");
                        gestionEvenement("RETR "+table_mails.getValueAt(table_mails.getSelectedRow(), 0).toString());
                    }
                    
                }
                else
                {
                    if(JOptionPane.showConfirmDialog(null, "Etes-vous sur de vouloir supprimer ce message ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    {
                        gestionEvenement("DELE "+table_mails.getValueAt(table_mails.getSelectedRow(), 0).toString());
                    }
                }
                //System.out.println("message selected : "+lastMessageSelected);
            }
        });
        loadFichierCache();
    }
    
    private void gestionEvenement(String evenement)
    {
        if(evenement.equals("LIST"))
        {
            envoiMessage(evenement);
            recoitMessage();
        }
        else if (evenement.contains("RETR"))
        {
            envoiMessage(evenement);
            String mess1 = recoitMessageMail().replace("\r", "").replace("\n", "");
            System.out.println(mess1);
            table_mails.setValueAt(mess1.substring(mess1.indexOf("octets")+7), lastMessageSelected-1, 1);
            table_mails.setValueAt(mess1.split(" ")[1]+" octets", lastMessageSelected-1, 2);
            creerFichierCache(evenement.split(" ")[1]+" "+mess1.split(" ")[1]+" "+mess1.substring(mess1.indexOf("octets")+7));
        }
        else if (evenement.contains("DELE"))
        {
            envoiMessage(evenement);
            recoitMessage();
        }
        else if(evenement.equals("QUIT"))
        {
            envoiMessage(evenement);
            System.exit(0);
        }
        else
        {
            System.out.println("Commande inconnue");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_deco = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_mails = new javax.swing.JTable();
        lb_user = new javax.swing.JLabel();
        btn_clearCache = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        btn_deco.setText("Déconnexion");
        btn_deco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_decoActionPerformed(evt);
            }
        });

        table_mails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numéro", "Contenu", "Taille", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_mails.setRowSelectionAllowed(false);
        jScrollPane1.setViewportView(table_mails);

        lb_user.setText("Bienvenue, x !");

        btn_clearCache.setText("Nettoyer le cache");
        btn_clearCache.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_clearCacheActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lb_user, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 316, Short.MAX_VALUE)
                        .addComponent(btn_clearCache)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_deco))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_deco)
                    .addComponent(lb_user)
                    .addComponent(btn_clearCache))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 36, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_decoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_decoActionPerformed
        gestionEvenement("QUIT");
    }//GEN-LAST:event_btn_decoActionPerformed

    private void btn_clearCacheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_clearCacheActionPerformed
        supprimerFichierCache();
    }//GEN-LAST:event_btn_clearCacheActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_clearCache;
    private javax.swing.JButton btn_deco;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_user;
    private javax.swing.JTable table_mails;
    // End of variables declaration//GEN-END:variables

    private void envoiMessage(String message)
    {
        System.out.println(message+" ->");
        out.write(message+"\r\n");
        out.flush();
    }
    
    private String recoitMessage()
    {
        String result = "";
        try {
            char c = (char) in.read();
            while(!result.endsWith("\r"))
            {
                result += c;
                c = (char)in.read();    
            }
        } catch(SocketTimeoutException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("<- " + result);
        
        
        return result;
    }
    
    private String recoitMessageMail()
    {
        String result = "";
        try {
            char c = (char) in.read();
            while(!result.endsWith(".\r"))
            {
                result += c;
                c = (char)in.read();
            }
        } catch(SocketTimeoutException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (IOException ex) {
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        } 
        System.out.println("<- " + result);
        return result;
    }
    
    private void creationLignesMessages(int nbMessages)
    {
        for(int i=1; i<=nbMessages; i++)
        {
            DefaultTableModel model = (DefaultTableModel) table_mails.getModel();
            model.addRow(new Object[]{i, "Cliquez pour voir le message", "", "X"});
        }
    }
    
    private void creerFichierCache(String message)
    {
        try {
            if(!new File(FileSystems.getDefault().getPath(System.getProperty("user.dir")+"/"+utilisateur+".txt").toString()).exists())
            {
                Files.createFile(FileSystems.getDefault().getPath(System.getProperty("user.dir")+"/"+utilisateur+".txt"));
            }

            List<String> lines = Arrays.asList(message);
            Path file = FileSystems.getDefault().getPath(System.getProperty("user.dir")+"/"+utilisateur+".txt");
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null,"Le fichier d'un utilisateur n'a pas pu être atteint (problème de droits)");
            Logger.getLogger(Connexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void supprimerFichierCache()
    {
        if(new File(FileSystems.getDefault().getPath(System.getProperty("user.dir")+"/"+utilisateur+".txt").toString()).exists())
        {
            try {
                Files.delete(FileSystems.getDefault().getPath(System.getProperty("user.dir")+"/"+utilisateur+".txt"));
            } catch (IOException ex) {
                Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            }
            messages.clear();
        }
    }
    
    private void loadFichierCache()
    {
        if(new File(FileSystems.getDefault().getPath(System.getProperty("user.dir")+"/"+utilisateur+".txt").toString()).exists())
        {
            Charset charset = Charset.forName("UTF-8");
            try (BufferedReader reader = Files.newBufferedReader(FileSystems.getDefault().getPath(System.getProperty("user.dir")+"/"+utilisateur+".txt"), charset)) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] result = new String[2];
                    result[0] = line.split(" ")[1]+" octets";
                    System.out.println(line.substring(line.indexOf(" ")));
                    result[1] = line.substring(line.indexOf(" ")+1).substring(line.substring(line.indexOf(" ")+1).indexOf(" ")+1);
                    
                    messages.put(Integer.parseInt(line.split(" ")[0]), result);
                }
                System.out.println("Configuration chargée !");
            } catch (IOException x) {
                JOptionPane.showMessageDialog(null,"La configuration n'a pas pu être chargée. Le fichier n'existe peut-être pas encore.");
            }
        }
    }
}
