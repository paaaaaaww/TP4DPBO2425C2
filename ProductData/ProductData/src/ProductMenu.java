import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Font;


public class ProductMenu extends JFrame {
    public static void main(String[] args) {
        // buat object window
        ProductMenu menu = new ProductMenu();

        // atur ukuran window
        menu.setSize(700, 600);

        // letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // isi window
        menu.setContentPane(menu.mainPanel);

        // ubah warna background
        menu.mainPanel.setBackground(Color.WHITE);

        // tampilkan window
        menu.setVisible(true);

        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua produk
    private ArrayList<Product> listProduct;

    public JPanel mainPanel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField hargaField;
    private JTextField merkField;
    private JTable productTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox<String> kategoriBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel namaLabel;
    private JLabel hargaLabel;
    private JLabel kategoriLabel;
    private JLabel merkLabel;
    private JComboBox<String> SkinTypeBox;
    private JComboBox<String> finishProdukBox;



    // constructor
    public ProductMenu() {
        // inisialisasi listProduct
        listProduct = new ArrayList<>();

        // isi listProduct awal
        populateList();

        // isi tabel produk
        productTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box kategori
        String[] kategoriData = { "Pilih Kategori", "Skincare", "Make Up", "Tools Make Up" };
        kategoriBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // atur isi combo box skin type (pakai yang dari desain, bukan add manual!)
        SkinTypeBox.setModel(new DefaultComboBoxModel<>(new String[]{
                "Oily", "Dry", "Combination", "All Skin Types"
        }));


        // tombol delete saat awal
        deleteButton.setVisible(true);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData(); // tambah data baru
                } else {
                    updateData(); // update data yang dipilih
                }
            }
        });

        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });

        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // saat baris tabel ditekan
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedIndex = productTable.getSelectedRow();

                // ambil data dari tabel
                String curId = productTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = productTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curHarga = productTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curKategori = productTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curSkinType = productTable.getModel().getValueAt(selectedIndex, 5).toString();
                String curMerk = productTable.getModel().getValueAt(selectedIndex, 6).toString();


                // tampilkan ke field
                idField.setText(curId);
                namaField.setText(curNama);
                hargaField.setText(curHarga);
                kategoriBox.setSelectedItem(curKategori);
                SkinTypeBox.setSelectedItem(curSkinType);
                merkField.setText(curMerk);

                // ubah tombol jadi "Update"
                addUpdateButton.setText("Update");

                // tampilkan tombol delete
                deleteButton.setVisible(true);
            }
        });
    }


    public final DefaultTableModel setTable() {
        // tentukan kolom tabel (sekarang ada 7 kolom)
        Object[] cols = { "No", "ID Produk", "Nama", "Harga", "Kategori", "Skin Type", "Merk", };

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        // isi tabel dengan listProduct
        for (int i = 0; i < listProduct.size(); i++) {
            Object[] row = {
                    i + 1,
                    listProduct.get(i).getId(),
                    listProduct.get(i).getNama(),
                    String.format("%.2f", listProduct.get(i).getHarga()),
                    listProduct.get(i).getKategori(),
                    listProduct.get(i).getSkinType(),
                    listProduct.get(i).getMerk(),

            };
            tmp.addRow(row);
        }

        return tmp;
    }


    public void insertData() {
        try {
            // ambil value dari textfield dan combobox
            String id = idField.getText();
            String nama = namaField.getText();
            double harga = Double.parseDouble(hargaField.getText());
            String kategori = kategoriBox.getSelectedItem().toString();
            String SkinType = SkinTypeBox.getSelectedItem().toString();
            String Merk = merkField.getText();


            // tambahkan data ke dalam list
            listProduct.add(new Product(id, nama, harga, kategori, SkinType, Merk));

            // update tabel
            productTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Insert Berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!");
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public void updateData() {
        try {
            // ambil value dari textfield dan combobox
            String id = idField.getText();
            String nama = namaField.getText();
            double harga = Double.parseDouble(hargaField.getText());
            String kategori = kategoriBox.getSelectedItem().toString();
            String SkinType = SkinTypeBox.getSelectedItem().toString();
            String Merk = merkField.getText();

            // tambahkan data ke dalam list
            listProduct.get(selectedIndex).setId(id);
            listProduct.get(selectedIndex).setNama(nama);
            listProduct.get(selectedIndex).setHarga(harga);
            listProduct.get(selectedIndex).setKategori(kategori);
            listProduct.get(selectedIndex).setSkinType(SkinType);
            listProduct.get(selectedIndex).setMerk(Merk);


            // update tabel
            productTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Update Berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil diupdate!");
        }catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }


    }

    public void deleteData() {
        // Tambahkan konfirmasi dulu
        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Apakah kamu yakin ingin menghapus produk ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        // Hanya hapus data kalau user menekan "Yes"
        if (confirm == JOptionPane.YES_OPTION) {
            // hapus data dari list
            listProduct.remove(selectedIndex);

            // update tabel
            productTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Delete Berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        } else {
            // kalau user klik "No", tampilkan pesan batal (opsional)
            JOptionPane.showMessageDialog(null, "Penghapusan dibatalkan.");
        }
    }


    public void clearForm() {
        // kosongkan semua texfield dan combo box
        idField.setText("");
        namaField.setText("");
        hargaField.setText("");
        kategoriBox.setSelectedIndex(0);
        SkinTypeBox.setSelectedIndex(0);
        merkField.setText("");

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;

    }

    // panggil prosedur ini untuk mengisi list produk
    private void populateList() {
        listProduct.add(new Product("P001", "Hydrating Toner", 85000.0, "Skincare", "Dry", "Garnier"));
        listProduct.add(new Product("P002", "Oil Control Serum", 120000.0, "Skincare", "Oily", "Something"));
        listProduct.add(new Product("P003", "Brightening Essence", 150000.0, "Skincare", "All Skin Types", "Avoskin"));
        listProduct.add(new Product("P004", "Matte Foundation", 180000.0, "Make Up", "Oily", "Studio Tropik"));
        listProduct.add(new Product("P005", "Moisturizing Cream", 95000.0, "Skincare", "Dry", "Elformula"));
        listProduct.add(new Product("P006", "Cushion Compact", 200000.0, "Make Up", "Combination", "Make Over"));
        listProduct.add(new Product("P007", "Lip Tint", 75000.0, "Make Up", "All Skin Types", "MOP"));
        listProduct.add(new Product("P008", "Sunscreen SPF 50", 130000.0, "Skincare", "All Skin Types", "Labore"));
        listProduct.add(new Product("P009", "Makeup Brush", 100000.0, "Tools Make Up", "All Skin Types", "Aeris"));
        listProduct.add(new Product("P010", "Beauty Blender", 50000.0, "Tools Make Up", "All Skin Types", "Maange"));
    }
}