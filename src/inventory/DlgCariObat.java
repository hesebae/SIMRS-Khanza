/*
  Dilarang keras menggandakan/mengcopy/menyebarkan/membajak/mendecompile 
  Software ini dalam bentuk apapun tanpa seijin pembuat software
  (Khanza.Soft Media). Bagi yang sengaja membajak softaware ini ta
  npa ijin, kami sumpahi sial 1000 turunan, miskin sampai 500 turu
  nan. Selalu mendapat kecelakaan sampai 400 turunan. Anak pertama
  nya cacat tidak punya kaki sampai 300 turunan. Susah cari jodoh
  sampai umur 50 tahun sampai 200 turunan. Ya Alloh maafkan kami 
  karena telah berdoa buruk, semua ini kami lakukan karena kami ti
  dak pernah rela karya kami dibajak tanpa ijin.
 */

package inventory;

import fungsi.WarnaTable2;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.var;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import simrskhanza.DlgResepObat;
import widget.Button;

/**
 *
 * @author dosen
 */
public final class DlgCariObat extends javax.swing.JDialog {
    private final DefaultTableModel tabModeobat;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();
    private PreparedStatement psobat,psobatsimpan,psobatsimpan2,psobatsimpan3,pshapusobat,pscarikapasitas,psobatasuransi,psstok;
    private ResultSet rsobat,carikapasitas,rsstok;
    private double x=0,y=0,embalase=0,tuslah=0,kenaikan=0,stokbarang=0,ttl=0,ppnobat=0;
    private int i=0,z=0;
    private boolean[] pilih; 
    private double[] jumlah,harga,eb,ts,stok,beli;
    private String[] kodebarang,namabarang,kodesatuan,letakbarang,namajenis,aturan,industri;
    private String bangsal=Sequel.cariIsi("select kd_bangsal from set_lokasi limit 1"),tampilkan_ppnobat_ralan="";
    public DlgBarang barang=new DlgBarang(null,false);
    public DlgAturanPakai aturanpakai=new DlgAturanPakai(null,false);
    private WarnaTable2 warna=new WarnaTable2();
    /** Creates new form DlgPenyakit
     * @param parent
     * @param modal */
    public DlgCariObat(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(10,2);
        setSize(656,250);

        Object[] row={"K","Jumlah","Kode Barang","Nama Barang","Satuan","Letak Barang","Harga(Rp)","Jenis Obat","Embalase","Tuslah","Stok","Aturan Pakai","I.F.","H.Beli"};
        tabModeobat=new DefaultTableModel(null,row){
            @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if ((colIndex==0)||(colIndex==1)||(colIndex==8)||(colIndex==9)||(colIndex==11)) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class, 
                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Object.class,
                java.lang.Object.class,java.lang.Double.class
             };
             /*Class[] types = new Class[] {
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };*/
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbObat.setModel(tabModeobat);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 14; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(45);
            }else if(i==2){
                column.setPreferredWidth(75);
            }else if(i==3){
                column.setPreferredWidth(200);
            }else if(i==4){
                column.setPreferredWidth(75);
            }else if(i==5){
                column.setPreferredWidth(80);
            }else if(i==6){
                column.setPreferredWidth(85);
            }else if(i==7){
                column.setPreferredWidth(75);
            }else if(i==8){
                column.setPreferredWidth(60);
            }else if(i==9){
                column.setPreferredWidth(60);
            }else if(i==10){
                column.setPreferredWidth(40);
            }else if(i==11){
                column.setPreferredWidth(130);
            }else if(i==12){
                column.setPreferredWidth(130);
            }else if(i==13){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }                 
        }
        warna.kolom=1;
        tbObat.setDefaultRenderer(Object.class,warna);
        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
        if(koneksiDB.cariCepat().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {tampilobat();}
                @Override
                public void removeUpdate(DocumentEvent e) {tampilobat();}
                @Override
                public void changedUpdate(DocumentEvent e) {tampilobat();}
            });
        }
        
        aturanpakai.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(aturanpakai.getTable().getSelectedRow()!= -1){    
                    tbObat.setValueAt(aturanpakai.getTable().getValueAt(aturanpakai.getTable().getSelectedRow(),0).toString(),tbObat.getSelectedRow(),11);
                }   
                tbObat.requestFocus();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        tampilkan_ppnobat_ralan=Sequel.cariIsi("select tampilkan_ppnobat_ralan from set_nota");         
    }    
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Popup = new javax.swing.JPopupMenu();
        ppBersihkan = new javax.swing.JMenuItem();
        ppStok = new javax.swing.JMenuItem();
        Kd2 = new widget.TextBox();
        TNoRw = new widget.TextBox();
        Tanggal = new widget.TextBox();
        Jam = new widget.TextBox();
        KdPj = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        panelisi3 = new widget.panelisi();
        label9 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        BtnTambah = new widget.Button();
        BtnSeek5 = new widget.Button();
        BtnSimpan = new widget.Button();
        label13 = new widget.Label();
        BtnKeluar = new widget.Button();
        FormInput = new widget.PanelBiasa();
        jLabel5 = new widget.Label();
        LTotal = new widget.Label();
        jLabel6 = new widget.Label();
        LPpn = new widget.Label();
        jLabel7 = new widget.Label();
        LTotalTagihan = new widget.Label();
        label12 = new widget.Label();
        Jenisjual = new widget.ComboBox();
        ChkNoResep = new widget.CekBox();

        Popup.setName("Popup"); // NOI18N

        ppBersihkan.setBackground(new java.awt.Color(255, 255, 255));
        ppBersihkan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBersihkan.setForeground(new java.awt.Color(102, 51, 0));
        ppBersihkan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppBersihkan.setText("Bersihkan Jumlah");
        ppBersihkan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBersihkan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBersihkan.setIconTextGap(8);
        ppBersihkan.setName("ppBersihkan"); // NOI18N
        ppBersihkan.setPreferredSize(new java.awt.Dimension(200, 25));
        ppBersihkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBersihkanActionPerformed(evt);
            }
        });
        Popup.add(ppBersihkan);

        ppStok.setBackground(new java.awt.Color(255, 255, 255));
        ppStok.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppStok.setForeground(new java.awt.Color(102, 51, 0));
        ppStok.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppStok.setText("Tampilkan Semua Stok");
        ppStok.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppStok.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppStok.setIconTextGap(8);
        ppStok.setName("ppStok"); // NOI18N
        ppStok.setPreferredSize(new java.awt.Dimension(200, 25));
        ppStok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppStokActionPerformed(evt);
            }
        });
        Popup.add(ppStok);

        Kd2.setHighlighter(null);
        Kd2.setName("Kd2"); // NOI18N
        Kd2.setSelectionColor(new java.awt.Color(255, 255, 255));
        Kd2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Kd2KeyPressed(evt);
            }
        });

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.setSelectionColor(new java.awt.Color(255, 255, 255));

        Tanggal.setHighlighter(null);
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setSelectionColor(new java.awt.Color(255, 255, 255));

        Jam.setHighlighter(null);
        Jam.setName("Jam"); // NOI18N
        Jam.setSelectionColor(new java.awt.Color(255, 255, 255));

        KdPj.setHighlighter(null);
        KdPj.setName("KdPj"); // NOI18N
        KdPj.setSelectionColor(new java.awt.Color(255, 255, 255));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Obat, Alkes & BHP Medis ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 70, 40))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Dialog", 0, 11)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setComponentPopupMenu(Popup);
        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat.setComponentPopupMenu(Popup);
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbObat);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        panelisi3.setName("panelisi3"); // NOI18N
        panelisi3.setPreferredSize(new java.awt.Dimension(100, 43));
        panelisi3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        label9.setText("Key Word :");
        label9.setName("label9"); // NOI18N
        label9.setPreferredSize(new java.awt.Dimension(68, 23));
        panelisi3.add(label9);

        TCari.setToolTipText("Alt+C");
        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(315, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelisi3.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('1');
        BtnCari.setToolTipText("Alt+1");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelisi3.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('2');
        BtnAll.setToolTipText("Alt+2");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelisi3.add(BtnAll);

        BtnTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/plus_16.png"))); // NOI18N
        BtnTambah.setMnemonic('3');
        BtnTambah.setToolTipText("Alt+3");
        BtnTambah.setName("BtnTambah"); // NOI18N
        BtnTambah.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambahActionPerformed(evt);
            }
        });
        panelisi3.add(BtnTambah);

        BtnSeek5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/011.png"))); // NOI18N
        BtnSeek5.setMnemonic('4');
        BtnSeek5.setToolTipText("Alt+4");
        BtnSeek5.setName("BtnSeek5"); // NOI18N
        BtnSeek5.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnSeek5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeek5ActionPerformed(evt);
            }
        });
        BtnSeek5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSeek5KeyPressed(evt);
            }
        });
        panelisi3.add(BtnSeek5);

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        panelisi3.add(BtnSimpan);

        label13.setName("label13"); // NOI18N
        label13.setPreferredSize(new java.awt.Dimension(50, 23));
        panelisi3.add(label13);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('5');
        BtnKeluar.setToolTipText("Alt+5");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        panelisi3.add(BtnKeluar);

        internalFrame1.add(panelisi3, java.awt.BorderLayout.PAGE_END);

        FormInput.setBackground(new java.awt.Color(215, 225, 215));
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 43));
        FormInput.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 9));

        jLabel5.setText("Total :");
        jLabel5.setName("jLabel5"); // NOI18N
        jLabel5.setPreferredSize(new java.awt.Dimension(45, 23));
        FormInput.add(jLabel5);

        LTotal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LTotal.setText("0");
        LTotal.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        LTotal.setName("LTotal"); // NOI18N
        LTotal.setPreferredSize(new java.awt.Dimension(80, 23));
        FormInput.add(LTotal);

        jLabel6.setText("PPN :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(35, 23));
        FormInput.add(jLabel6);

        LPpn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LPpn.setText("0");
        LPpn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        LPpn.setName("LPpn"); // NOI18N
        LPpn.setPreferredSize(new java.awt.Dimension(65, 23));
        FormInput.add(LPpn);

        jLabel7.setText("Total+PPN :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(65, 23));
        FormInput.add(jLabel7);

        LTotalTagihan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LTotalTagihan.setText("0");
        LTotalTagihan.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        LTotalTagihan.setName("LTotalTagihan"); // NOI18N
        LTotalTagihan.setPreferredSize(new java.awt.Dimension(80, 23));
        FormInput.add(LTotalTagihan);

        label12.setText("Tarif :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(50, 23));
        FormInput.add(label12);

        Jenisjual.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Rawat Jalan", "Beli Luar", "Karyawan", "Utama/BPJS" }));
        Jenisjual.setName("Jenisjual"); // NOI18N
        Jenisjual.setPreferredSize(new java.awt.Dimension(100, 23));
        Jenisjual.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JenisjualItemStateChanged(evt);
            }
        });
        Jenisjual.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JenisjualKeyPressed(evt);
            }
        });
        FormInput.add(Jenisjual);

        ChkNoResep.setBorder(null);
        ChkNoResep.setForeground(new java.awt.Color(153, 0, 51));
        ChkNoResep.setSelected(true);
        ChkNoResep.setText("No.Resep   ");
        ChkNoResep.setBorderPainted(true);
        ChkNoResep.setBorderPaintedFlat(true);
        ChkNoResep.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ChkNoResep.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkNoResep.setName("ChkNoResep"); // NOI18N
        ChkNoResep.setOpaque(false);
        ChkNoResep.setPreferredSize(new java.awt.Dimension(85, 23));
        ChkNoResep.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChkNoResepItemStateChanged(evt);
            }
        });
        FormInput.add(ChkNoResep);

        internalFrame1.add(FormInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            tbObat.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampilobat();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampilobat();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnCari, TCari);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tbObat.getRowCount()!=0){
            try {
                getDataobat();
            } catch (java.lang.NullPointerException e) {
            }
            
            if(evt.getClickCount()==2){
                dispose();
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tbObat.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                try {
                    getDataobat();
                    i=tbObat.getSelectedColumn();
                    if(i==2){
                        try {
                            stokbarang=0;  
                            psstok=koneksi.prepareStatement("select ifnull(stok,'0') from gudangbarang where kd_bangsal=? and kode_brng=?");
                            try {
                                psstok.setString(1,bangsal);
                                psstok.setString(2,tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
                                rsstok=psstok.executeQuery();
                                if(rsstok.next()){
                                    stokbarang=rsstok.getDouble(1);
                                }                                
                            } catch (Exception e) {
                                stokbarang=0;
                                System.out.println("Notifikasi : "+e);
                            }finally{
                                if(rsstok != null){
                                    rsstok.close();
                                }
                                if(psstok != null){
                                    psstok.close();
                                }
                            }
                            
                            tbObat.setValueAt(stokbarang,tbObat.getSelectedRow(),10);
                            y=0;
                            try {
                                y=Double.parseDouble(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
                            } catch (Exception e) {
                                y=0;
                            }
                            if(stokbarang<y){
                                JOptionPane.showMessageDialog(rootPane,"Maaf stok tidak mencukupi..!!");
                                tbObat.setValueAt("",tbObat.getSelectedRow(),1);
                            }
                        } catch (Exception e) {
                            tbObat.setValueAt(0,tbObat.getSelectedRow(),10);
                        }   
                        TCari.setText("");
                        TCari.requestFocus();
                    }else if(i==8){
                        try {
                            if(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString().equals("0")||tbObat.getValueAt(tbObat.getSelectedRow(),8).toString().equals("")||tbObat.getValueAt(tbObat.getSelectedRow(),8).toString().equals("0.0")||tbObat.getValueAt(tbObat.getSelectedRow(),8).toString().equals("0,0")) {
                                tbObat.setValueAt(embalase,tbObat.getSelectedRow(),8);
                            }
                        } catch (Exception e) {
                            tbObat.setValueAt(0,tbObat.getSelectedRow(),8);
                        }
                    }else if(i==9){
                        try {
                            if(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("0")||tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("")||tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("0.0")||tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("0,0")) {
                                tbObat.setValueAt(tuslah,tbObat.getSelectedRow(),9);
                            }
                        } catch (Exception e) {
                            tbObat.setValueAt(0,tbObat.getSelectedRow(),9);
                        }
                            
                        TCari.setText("");
                        TCari.requestFocus();
                    }else if((i==10)||(i==3)){
                        ttl=0;
                        y=0;
                        int row2=tabModeobat.getRowCount();
                        for(int r=0;r<row2;r++){ 
                            try {
                                if(Double.parseDouble(tabModeobat.getValueAt(r,1).toString())>0){
                                    y=Double.parseDouble(tabModeobat.getValueAt(r,1).toString())*
                                      Double.parseDouble(tabModeobat.getValueAt(r,6).toString())+
                                      Double.parseDouble(tabModeobat.getValueAt(r,8).toString())+
                                      Double.parseDouble(tabModeobat.getValueAt(r,9).toString());
                                }                                 
                            } catch (Exception e) {
                                y=0;
                            }
                            ttl=ttl+y;
                        }
                        LTotal.setText(Valid.SetAngka(ttl));
                        ppnobat=0;
                        if(tampilkan_ppnobat_ralan.equals("Yes")){
                            ppnobat=ttl*0.1;
                            ttl=ttl+ppnobat;
                            LPpn.setText(Valid.SetAngka(ppnobat));
                            LTotalTagihan.setText(Valid.SetAngka(ttl));
                        }
                        TCari.setText("");
                        TCari.requestFocus();
                    }else if(i==11){
                        TCari.setText("");
                        TCari.requestFocus();
                    }
                } catch (java.lang.NullPointerException e) {
                }
            }else if((evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getDataobat();
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_DELETE){
                i=tbObat.getSelectedColumn();
                if((i==1)||(i==11)||(i==8)||(i==9)){
                    if(tbObat.getSelectedRow()!= -1){
                        tbObat.setValueAt("",tbObat.getSelectedRow(),i);
                    }
                }
                    
            }else if(evt.getKeyCode()==KeyEvent.VK_SHIFT){
                i=tbObat.getSelectedColumn();
                if(i!=11){
                    TCari.requestFocus();
                }                
            }else if(evt.getKeyCode()==KeyEvent.VK_RIGHT){
                i=tbObat.getSelectedColumn();
                if(i==2){
                    try {
                        stokbarang=0;                
                        psstok=koneksi.prepareStatement("select ifnull(stok,'0') from gudangbarang where kd_bangsal=? and kode_brng=?");
                        try {
                            psstok.setString(1,bangsal);
                            psstok.setString(2,tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
                            rsstok=psstok.executeQuery();
                            if(rsstok.next()){
                                stokbarang=rsstok.getDouble(1);                               
                            }                                
                        } catch (Exception e) {
                            stokbarang=0;
                            System.out.println("Notifikasi : "+e);
                        }finally{
                            if(rsstok != null){
                                rsstok.close();
                            }
                            if(psstok != null){
                                psstok.close();
                            }
                        }
                        tbObat.setValueAt(stokbarang,tbObat.getSelectedRow(),10);
                        
                        y=0;
                        try {
                            y=Double.parseDouble(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString());
                        } catch (Exception e) {
                            y=0;
                        }
                        if(stokbarang<y){
                            JOptionPane.showMessageDialog(rootPane,"Maaf stok tidak mencukupi..!!");
                            tbObat.setValueAt("",tbObat.getSelectedRow(),1);
                        }
                        
                        try {
                            if(tbObat.getValueAt(tbObat.getSelectedRow(),8).toString().equals("0")||tbObat.getValueAt(tbObat.getSelectedRow(),8).toString().equals("")||tbObat.getValueAt(tbObat.getSelectedRow(),8).toString().equals("0.0")||tbObat.getValueAt(tbObat.getSelectedRow(),8).toString().equals("0,0")) {
                                tbObat.setValueAt(embalase,tbObat.getSelectedRow(),8);
                            }
                        } catch (Exception e) {
                            tbObat.setValueAt(0,tbObat.getSelectedRow(),8);
                        }

                        try {
                            if(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("0")||tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("")||tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("0.0")||tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().equals("0,0")) {
                                tbObat.setValueAt(tuslah,tbObat.getSelectedRow(),9);
                            }
                        } catch (Exception e) {
                            tbObat.setValueAt(0,tbObat.getSelectedRow(),9);
                        }  
                    } catch (Exception e) {
                        tbObat.setValueAt(0,tbObat.getSelectedRow(),10);
                    }
                }else if(i==11){
                    var.setform("DlgCariObat");
                    aturanpakai.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                    aturanpakai.setLocationRelativeTo(internalFrame1);
                    aturanpakai.setVisible(true);
                }
            }   
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void Kd2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Kd2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_Kd2KeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambahActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        //barang.setModal(true);
        barang.emptTeks();
        barang.isCek();
        barang.setSize(internalFrame1.getWidth()+40,internalFrame1.getHeight()+40);
        barang.setLocationRelativeTo(internalFrame1);
        barang.setAlwaysOnTop(false);
        barang.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());           
    }//GEN-LAST:event_BtnTambahActionPerformed

private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().trim().equals("")){
            Valid.textKosong(TCari,"Data");
        }else if(bangsal.equals("")){
            Valid.textKosong(TCari,"Lokasi");
        }else{
            try {  
                koneksi.setAutoCommit(false);                  
                for(i=0;i<tbObat.getRowCount();i++){ 
                    if(Valid.SetAngka(tbObat.getValueAt(i,1).toString())>0){
                        pscarikapasitas= koneksi.prepareStatement("select IFNULL(kapasitas,1) from databarang where kode_brng=?");              
                        psobatsimpan= koneksi.prepareStatement("insert into detail_pemberian_obat values(?,?,?,?,?,?,?,?,?,?)");
                        psobatsimpan2= koneksi.prepareStatement("insert into gudangbarang values(?,?,?)");
                        psobatsimpan3= koneksi.prepareStatement("update gudangbarang set stok=stok-? where kode_brng=? and kd_bangsal=?");                        
                        try {
                            psobatsimpan.setString(1,Tanggal.getText());
                            psobatsimpan.setString(2,Jam.getText());
                            psobatsimpan.setString(3,TNoRw.getText());
                            psobatsimpan.setString(4,tbObat.getValueAt(i,2).toString());
                            psobatsimpan.setString(5,tbObat.getValueAt(i,13).toString());
                            psobatsimpan.setString(6,tbObat.getValueAt(i,6).toString());
                            if(tbObat.getValueAt(i,0).toString().equals("true")){
                                pscarikapasitas.setString(1,tbObat.getValueAt(i,2).toString());
                                carikapasitas=pscarikapasitas.executeQuery();
                                if(carikapasitas.next()){                                    
                                    psobatsimpan.setDouble(7,(Double.parseDouble(tbObat.getValueAt(i,1).toString())/carikapasitas.getDouble(1)));
                                    psobatsimpan.setDouble(10,Double.parseDouble(tbObat.getValueAt(i,8).toString())+
                                            Double.parseDouble(tbObat.getValueAt(i,9).toString())+
                                            (Double.parseDouble(tbObat.getValueAt(i,6).toString())*
                                                    (Double.parseDouble(tbObat.getValueAt(i,1).toString())/carikapasitas.getDouble(1))));                            
                                    try{
                                       psobatsimpan2.setString(1,tbObat.getValueAt(i,2).toString());
                                       psobatsimpan2.setString(2,bangsal);
                                       psobatsimpan2.setDouble(3,-(Double.parseDouble(tbObat.getValueAt(i,1).toString())/carikapasitas.getDouble(1)));
                                       psobatsimpan2.executeUpdate();
                                    }catch(Exception ex){
                                        psobatsimpan3.setDouble(1,(Double.parseDouble(tbObat.getValueAt(i,1).toString())/carikapasitas.getDouble(1)));
                                        psobatsimpan3.setString(2,tbObat.getValueAt(i,2).toString());
                                        psobatsimpan3.setString(3,bangsal);
                                        psobatsimpan3.executeUpdate();                                 
                                    }
                                }
                            }else{                                
                                psobatsimpan.setDouble(7,Double.parseDouble(tbObat.getValueAt(i,1).toString()));
                                psobatsimpan.setDouble(10,Double.parseDouble(tbObat.getValueAt(i,8).toString())+
                                        Double.parseDouble(tbObat.getValueAt(i,9).toString())+
                                        (Double.parseDouble(tbObat.getValueAt(i,6).toString())*Double.parseDouble(tbObat.getValueAt(i,1).toString())));
                                try{
                                   psobatsimpan2.setString(1,tbObat.getValueAt(i,2).toString());
                                   psobatsimpan2.setString(2,bangsal);
                                   psobatsimpan2.setDouble(3,-Double.parseDouble(tbObat.getValueAt(i,1).toString()));
                                   psobatsimpan2.executeUpdate();
                                }catch(Exception ex){
                                    psobatsimpan3.setDouble(1,Double.parseDouble(tbObat.getValueAt(i,1).toString()));
                                    psobatsimpan3.setString(2,tbObat.getValueAt(i,2).toString());
                                    psobatsimpan3.setString(3,bangsal);
                                    psobatsimpan3.executeUpdate();                                 
                                }
                            }
                            psobatsimpan.setString(8,tbObat.getValueAt(i,8).toString());
                            psobatsimpan.setString(9,tbObat.getValueAt(i,9).toString());
                            psobatsimpan.executeUpdate();  
                            Sequel.menyimpan("aturan_pakai","?,?,?,?,?",5,new String[]{
                                Tanggal.getText(),Jam.getText(),TNoRw.getText(),tbObat.getValueAt(i,2).toString(),tbObat.getValueAt(i,11).toString()
                            });
                        } catch (Exception e) {
                            System.out.println("Input Obat Gagal Karena : "+e);  
                            JOptionPane.showMessageDialog(null,"Maaf, gagal menyimpan data. Kemungkinan ada data yang sama dimasukkan sebelumnya...!");
                        }finally{
                            if(carikapasitas != null){
                                carikapasitas.close();
                            }
                            if(pscarikapasitas != null){
                                pscarikapasitas.close();
                            }
                            if(psobatsimpan != null){
                                psobatsimpan.close();
                            }
                            if(psobatsimpan2 != null){
                                psobatsimpan2.close();
                            }
                            if(psobatsimpan3 != null){
                                psobatsimpan3.close();
                            }
                        }                        
                    }
                    tbObat.setValueAt("",i,1);
                }  
                koneksi.setAutoCommit(true);
                LTotal.setText("0");
                LPpn.setText("0");
                LTotalTagihan.setText("0");
                if(ChkNoResep.isSelected()==true){
                    DlgResepObat resep=new DlgResepObat(null,false);
                    resep.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                    resep.setLocationRelativeTo(internalFrame1);
                    resep.emptTeks(); 
                    resep.isCek();
                    resep.setNoRm(TNoRw.getText(),Valid.SetTgl2(Tanggal.getText()),Valid.SetTgl2(Tanggal.getText()),Jam.getText().substring(0,2),Jam.getText().substring(3,5),Jam.getText().substring(6,8));
                    resep.tampil();
                    resep.setVisible(true);
                }
                dispose();
            } catch (Exception ex) {
                System.out.println(ex);                
            }
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

private void BtnSeek5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeek5ActionPerformed
    DlgCariKonversi carikonversi=new DlgCariKonversi(null,false);
    carikonversi.setLocationRelativeTo(internalFrame1);
    carikonversi.setAlwaysOnTop(false);
    carikonversi.setVisible(true);
}//GEN-LAST:event_BtnSeek5ActionPerformed

private void BtnSeek5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSeek5KeyPressed
// TODO add your handling code here:
}//GEN-LAST:event_BtnSeek5KeyPressed

private void ppBersihkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBersihkanActionPerformed
            for(i=0;i<tbObat.getRowCount();i++){ 
                tbObat.setValueAt("",i,1);
                tbObat.setValueAt(0,i,10);
                tbObat.setValueAt(0,i,9);
                tbObat.setValueAt(0,i,8);
            }
            LTotal.setText("0");
}//GEN-LAST:event_ppBersihkanActionPerformed

private void JenisjualItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JenisjualItemStateChanged
       tampilobat(); 
}//GEN-LAST:event_JenisjualItemStateChanged

private void JenisjualKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JenisjualKeyPressed
        Valid.pindah(evt, TCari,BtnKeluar);
}//GEN-LAST:event_JenisjualKeyPressed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        emptTeksobat();
        embalase=Sequel.cariIsiAngka("select embalase_per_obat from set_embalase");
        tuslah=Sequel.cariIsiAngka("select tuslah_per_obat from set_embalase");
    }//GEN-LAST:event_formWindowActivated

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        tampilobat();
    }//GEN-LAST:event_formWindowOpened

    private void ChkNoResepItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ChkNoResepItemStateChanged
        if(ChkNoResep.isSelected()==true){
                    DlgResepObat resep=new DlgResepObat(null,false);
                    resep.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                    resep.setLocationRelativeTo(internalFrame1);
                    resep.emptTeks(); 
                    resep.isCek();
                    resep.setNoRm(TNoRw.getText(),Valid.SetTgl2(Tanggal.getText()),Valid.SetTgl2(Tanggal.getText()),Jam.getText().substring(0,2),Jam.getText().substring(3,5),Jam.getText().substring(6,8));
                    resep.tampil();
                    resep.setVisible(true);
        }
    }//GEN-LAST:event_ChkNoResepItemStateChanged

    private void ppStokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppStokActionPerformed
        for(i=0;i<tbObat.getRowCount();i++){ 
            try {
                stokbarang=0; 
                psstok=koneksi.prepareStatement("select ifnull(stok,'0') from gudangbarang where kd_bangsal=? and kode_brng=?");
                try {
                    psstok.setString(1,bangsal);
                    psstok.setString(2,tbObat.getValueAt(i,2).toString());
                    rsstok=psstok.executeQuery();
                    if(rsstok.next()){
                        stokbarang=rsstok.getDouble(1);
                    }
                } catch (Exception e) {
                    stokbarang=0;
                    System.out.println("Notifikasi : "+e);
                }finally{
                    if(rsstok != null){
                        rsstok.close();
                    }
                    
                    if(psstok != null){
                        psstok.close();
                    }
                }
                
                tbObat.setValueAt(stokbarang,i,10);
            } catch (Exception e) {
                tbObat.setValueAt(0,i,10);
            }
        }   
    }//GEN-LAST:event_ppStokActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgCariObat dialog = new DlgCariObat(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnAll;
    private widget.Button BtnCari;
    private widget.Button BtnKeluar;
    private widget.Button BtnSeek5;
    private widget.Button BtnSimpan;
    private widget.Button BtnTambah;
    private widget.CekBox ChkNoResep;
    private widget.PanelBiasa FormInput;
    private widget.TextBox Jam;
    private widget.ComboBox Jenisjual;
    private widget.TextBox Kd2;
    private widget.TextBox KdPj;
    private widget.Label LPpn;
    private widget.Label LTotal;
    private widget.Label LTotalTagihan;
    private javax.swing.JPopupMenu Popup;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.TextBox TNoRw;
    private widget.TextBox Tanggal;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel5;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label label12;
    private widget.Label label13;
    private widget.Label label9;
    private widget.panelisi panelisi3;
    private javax.swing.JMenuItem ppBersihkan;
    private javax.swing.JMenuItem ppStok;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables

    public void tampilobat() {        
        z=0;
        for(i=0;i<tbObat.getRowCount();i++){
            if(!tbObat.getValueAt(i,0).toString().equals("")){
                z++;
            }
        }    
        
        pilih=null;
        pilih=new boolean[z]; 
        jumlah=null;
        jumlah=new double[z];
        harga=null;
        harga=new double[z];
        eb=null;
        eb=new double[z];
        ts=null;
        ts=new double[z];
        stok=null;
        stok=new double[z];
        kodebarang=null;
        kodebarang=new String[z];
        namabarang=null;
        namabarang=new String[z];
        kodesatuan=null;
        kodesatuan=new String[z];
        letakbarang=null;
        letakbarang=new String[z];
        namajenis=null;
        namajenis=new String[z];                   
        aturan=null;
        aturan=new String[z];           
        industri=null;
        industri=new String[z];         
        beli=null;
        beli=new double[z]; 
        z=0;        
        for(i=0;i<tbObat.getRowCount();i++){
            if(!tbObat.getValueAt(i,1).toString().equals("")){
                pilih[z]=Boolean.parseBoolean(tbObat.getValueAt(i,0).toString());                
                try {
                    jumlah[z]=Double.parseDouble(tbObat.getValueAt(i,1).toString());
                } catch (Exception e) {
                    jumlah[z]=0;
                }  
                kodebarang[z]=tbObat.getValueAt(i,2).toString();
                namabarang[z]=tbObat.getValueAt(i,3).toString();
                kodesatuan[z]=tbObat.getValueAt(i,4).toString();
                letakbarang[z]=tbObat.getValueAt(i,5).toString();
                try {
                    harga[z]=Double.parseDouble(tbObat.getValueAt(i,6).toString());
                } catch (Exception e) {
                    harga[z]=0;
                }                  
                namajenis[z]=tbObat.getValueAt(i,7).toString();
                try {
                    eb[z]=Double.parseDouble(tbObat.getValueAt(i,8).toString());
                } catch (Exception e) {
                    eb[z]=0;
                }  
                try {
                    ts[z]=Double.parseDouble(tbObat.getValueAt(i,9).toString());
                } catch (Exception e) {
                    ts[z]=0;
                } 
                try {
                    stok[z]=Double.parseDouble(tbObat.getValueAt(i,10).toString());
                } catch (Exception e) {
                    stok[z]=0;
                } 
                aturan[z]=tbObat.getValueAt(i,11).toString();
                industri[z]=tbObat.getValueAt(i,12).toString();
                try {
                    beli[z]=Double.parseDouble(tbObat.getValueAt(i,13).toString());
                } catch (Exception e) {
                    beli[z]=0;
                } 
                z++;
            }
        }
        
        Valid.tabelKosong(tabModeobat);             
        
        for(i=0;i<z;i++){
            tabModeobat.addRow(new Object[] {
                pilih[i],jumlah[i],kodebarang[i],namabarang[i],kodesatuan[i],letakbarang[i],harga[i],namajenis[i],eb[i],ts[i],stok[i],aturan[i],industri[i],beli[i]
            });
        }
        
        try {
            psobat=koneksi.prepareStatement("select databarang.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,databarang.karyawan,databarang.ralan,databarang.beliluar,"+
                        " databarang.letak_barang,databarang.utama,industrifarmasi.nama_industri,databarang.h_beli from databarang inner join jenis inner join industrifarmasi on databarang.kdjns=jenis.kdjns "+
                        " and industrifarmasi.kode_industri=databarang.kode_industri where databarang.status='1' and databarang.kode_brng like ? or "+
                        " databarang.status='1' and databarang.nama_brng like ? or "+
                        " databarang.status='1' and jenis.nama like ? order by databarang.nama_brng");
            psobatasuransi=koneksi.prepareStatement("select databarang.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,(databarang.h_beli+(databarang.h_beli*?)) as harga,"+
                        " databarang.letak_barang,industrifarmasi.nama_industri,databarang.h_beli from databarang inner join jenis inner join industrifarmasi on databarang.kdjns=jenis.kdjns "+
                        " and industrifarmasi.kode_industri=databarang.kode_industri where databarang.status='1' and databarang.kode_brng like ? or "+
                        " databarang.status='1' and databarang.nama_brng like ? or "+
                        " databarang.status='1' and jenis.nama like ? order by databarang.nama_brng");
            try{    
                if(kenaikan>0){
                    psobatasuransi.setDouble(1,kenaikan);
                    psobatasuransi.setString(2,"%"+TCari.getText().trim()+"%");
                    psobatasuransi.setString(3,"%"+TCari.getText().trim()+"%");
                    psobatasuransi.setString(4,"%"+TCari.getText().trim()+"%");
                    rsobat=psobatasuransi.executeQuery();
                    while(rsobat.next()){
                            tabModeobat.addRow(new Object[] {false,"",rsobat.getString("kode_brng"),rsobat.getString("nama_brng"),
                                       rsobat.getString("kode_sat"),rsobat.getString("letak_barang"),Math.round(rsobat.getDouble("harga")),
                                       rsobat.getString("nama"),0,0,0,"",rsobat.getString("nama_industri"),
                                       Math.round(rsobat.getDouble("h_beli"))
                            });          
                    }                 
                }else{
                    psobat.setString(1,"%"+TCari.getText().trim()+"%");
                    psobat.setString(2,"%"+TCari.getText().trim()+"%");
                    psobat.setString(3,"%"+TCari.getText().trim()+"%");
                    rsobat=psobat.executeQuery();
                    while(rsobat.next()){
                        if(Jenisjual.getSelectedItem().equals("Karyawan")){
                            tabModeobat.addRow(new Object[] {false,"",rsobat.getString("kode_brng"),rsobat.getString("nama_brng"),
                                       rsobat.getString("kode_sat"),rsobat.getString("letak_barang"),rsobat.getDouble("karyawan"),
                                       rsobat.getString("nama"),0,0,0,"",rsobat.getString("nama_industri"),
                                       Math.round(rsobat.getDouble("h_beli"))
                            });
                        }else if(Jenisjual.getSelectedItem().equals("Rawat Jalan")){
                            tabModeobat.addRow(new Object[] {false,"",rsobat.getString("kode_brng"),rsobat.getString("nama_brng"),
                                       rsobat.getString("kode_sat"),rsobat.getString("letak_barang"),rsobat.getDouble("ralan"),
                                       rsobat.getString("nama"),0,0,0,"",rsobat.getString("nama_industri"),
                                       Math.round(rsobat.getDouble("h_beli"))
                            });
                        }else if(Jenisjual.getSelectedItem().equals("Beli Luar")){
                            tabModeobat.addRow(new Object[] {false,"",rsobat.getString("kode_brng"),rsobat.getString("nama_brng"),
                                       rsobat.getString("kode_sat"),rsobat.getString("letak_barang"),rsobat.getDouble("beliluar"),
                                       rsobat.getString("nama"),0,0,0,"",rsobat.getString("nama_industri"),
                                       Math.round(rsobat.getDouble("h_beli"))
                            });
                        }else if(Jenisjual.getSelectedItem().equals("Utama/BPJS")){
                            tabModeobat.addRow(new Object[] {false,"",rsobat.getString("kode_brng"),rsobat.getString("nama_brng"),
                                       rsobat.getString("kode_sat"),rsobat.getString("letak_barang"),rsobat.getDouble("utama"),
                                       rsobat.getString("nama"),0,0,0,"",rsobat.getString("nama_industri"),
                                       Math.round(rsobat.getDouble("h_beli"))
                            });
                        }                 
                    }
                }              
            }catch(Exception e){
                System.out.println("Notifikasi : "+e);
            }finally{
                if(rsobat != null){
                    rsobat.close();
                }
                
                if(psobat != null){
                    psobat.close();
                }
                
                if(psobatasuransi != null){
                    psobatasuransi.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }
            
    }

    public void emptTeksobat() {
        Kd2.setText(""); 
        TCari.setText("");
        TCari.requestFocus();
    }

    private void getDataobat() {
        if(tbObat.getSelectedRow()!= -1){
            Kd2.setText("");
            Kd2.setText(tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
        }
    }

    public JTextField getTextField(){
        return Kd2;
    }

    public JTable getTable(){
        return tbObat;
    }
    
    public Button getButton(){
        return BtnSimpan;
    }
    
    public void isCek(){        
        BtnTambah.setEnabled(var.getobat());
        TCari.requestFocus();
    }
    
    public void setNoRm(String norwt,String tanggal, String jam) {        
        TNoRw.setText(norwt);
        Tanggal.setText(tanggal);
        Jam.setText(jam);  
        KdPj.setText(Sequel.cariIsi("select kd_pj from reg_periksa where no_rawat=?",norwt));
        kenaikan=Sequel.cariIsiAngka("select (hargajual/100) from set_harga_obat_ralan where kd_pj=?",KdPj.getText());
        TCari.requestFocus();
    }
    
    
}
