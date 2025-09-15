import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.Component;
import java.io.File;
import java.awt.Container;

import javax.swing.table.DefaultTableCellRenderer;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.Timer; // Timerの衝突を防ぐために明示的にjavax.swing.Timerをインポート
import java.util.Random;
import java.util.ArrayList;
import java.util.List;


public class AppDiary{
	
    //private static Set<String> collectedItems = new HashSet<>();
    private static JLabel itemLabel= new JLabel("🎁 アイテムを獲得しよう 🎁");
    private static JTextArea collectionTextArea;
    private static JTextArea diaryText;
    private static JTextArea diaryDisplay;
    private static JCalendar calendar;
    private static final String DIARY_FOLDER = "diary_entries";
    private static JLabel selectedDateLabel;
    private  static JLabel catLabel;  // ここで catLabel をクラスのメンバ変数として定義
    private static List<String> collectedItems = new ArrayList<>(); // アイテム名リスト
    private static List<String> collectedImages = new ArrayList<>(); // アイテム画像パスリスト
    private static JFrame frame;
    private static JPanel collectionItemPanel; // アイテム表示用のパネル
    
 // アプリケーションの初期化
    public static void initializePanel() {
    	frame = new JFrame("Diary app");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

 

        // collectionItemPanelの初期化
        if (collectionItemPanel == null) {
        System.out.println("Initializing collectionItemPanel...");
        collectionItemPanel = new JPanel();
        collectionItemPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // パネルをフレームや他のコンテナに追加
        frame.getContentPane().add(new JScrollPane(collectionItemPanel), BorderLayout.CENTER);
        System.out.println("collectionItemPanel initialized.");
        } else {
            System.out.println("collectionItemPanel is already initialized.");
        }
        
        
        
        // JFrameを表示
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        
        new File(DIARY_FOLDER).mkdir(); // 日記保存フォルダ作成（なければ）

        // メインウィンドウ
        JFrame frame = new JFrame("Diary app");
        // クラスパスからリソースとして画像ファイルを読み込む
        ImageIcon appIcon = new ImageIcon("C:\\Users\\dnst3\\eclipse-workspace\\AppDiary\\src\\main\\resources\\images\\img.icon.png");
        frame.setIconImage(appIcon.getImage());


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.getContentPane().setBackground(new Color(237,231,246));//薄パープル

        // タブパネル作成
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(237,231,246));//薄パープル
        
        // ① 日記入力パネル
        JPanel diaryPanel = new JPanel(new BorderLayout());
        diaryPanel.setBackground(new Color(178,235,242));//薄ブルー

        // 左側（GIFを表示するパネル）
        ImageIcon catGif = new ImageIcon("C:\\Users\\dnst3\\eclipse-workspace\\AppDiary\\src\\main\\resources\\images\\image.cat4.gif");
        catLabel = new JLabel(catGif);  // クラスのメンバ変数 catLabelを初期化
        JPanel gifPanel = new JPanel(new BorderLayout());
        gifPanel.add(catLabel, BorderLayout.CENTER);
        gifPanel.setPreferredSize(new Dimension(400, 600));
        gifPanel.setBackground(new Color(252,228,236));//薄ピンク
        
        frame.add(gifPanel, BorderLayout.WEST);
        
        // 右側（日記入力エリアを含むパネル）
        JPanel textPanel = new JPanel(new BorderLayout()); // BorderLayoutに変更
        textPanel.setPreferredSize(new Dimension(400, 180)); // 高さを調整

        // 上下のバランスを取るための余白パネル
        JPanel topPadding = new JPanel();
        JPanel bottomPadding = new JPanel();
        topPadding.setPreferredSize(new Dimension(400, 100)); // 上に30pxの余白
        bottomPadding.setPreferredSize(new Dimension(400, 100)); // 下に30pxの余白

        // **左右の余白を追加**
        JPanel leftPadding = new JPanel();
        JPanel rightPadding = new JPanel();
        leftPadding.setPreferredSize(new Dimension(20, 120)); // 左に20pxの余白
        rightPadding.setPreferredSize(new Dimension(20, 120)); // 右に20pxの余白
        
        // JTextAreaの作成（行数を調整）
        diaryText = new JTextArea(6, 40); // 行数を6に増やす
        diaryText.setBackground(new Color(178, 235, 242)); // 薄ブルー
        diaryText.setForeground(Color.BLACK);
        diaryText.setFont(new Font("Serif", Font.PLAIN, 14));
        diaryText.setLineWrap(true); // 自動折り返しを設定
        diaryText.setWrapStyleWord(true); // 単語単位で折り返し

        // JScrollPane（スクロール付きエリア）の設定
        JScrollPane scrollPane = new JScrollPane(diaryText);
        scrollPane.setPreferredSize(new Dimension(380, 120)); // 横幅380、縦幅120
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // スクロールバーを常に表示

        // パネルに追加
        textPanel.add(topPadding, BorderLayout.NORTH);
        textPanel.add(scrollPane, BorderLayout.CENTER);
        textPanel.add(bottomPadding, BorderLayout.SOUTH);
        textPanel.add(leftPadding, BorderLayout.WEST);  // 左余白
        textPanel.add(rightPadding, BorderLayout.EAST); // 右余白

       // ① 日記入力パネルの背景色を変更（薄ピンク色）
        diaryPanel.setBackground(new Color(252,228,236));
        
       // ③ スクロールパネルの背景も調整（必要なら）
        scrollPane.getViewport().setBackground(new Color(252,228,236));
        
        // JSplitPaneで左右に分割
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gifPanel, textPanel);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.5);
        splitPane.setBackground(new Color(252,228,236));
        diaryPanel.add(splitPane, BorderLayout.CENTER);

        // 保存ボタン
        JButton saveButton = new JButton("保存");
        saveButton.setBackground(new Color(244, 194, 194));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Serif", Font.BOLD, 14));
        saveButton.addActionListener(e -> {
        saveDiary();
        startPopupTimer();
        });
        
             String item = collectRandomItem();
  //           collectedItems.add(item);
//            itemLabel.setText("🎁 新しいアイテム: " + item);

             

             saveCollection();
         
        diaryPanel.add(saveButton, BorderLayout.SOUTH);
        diaryPanel.add(itemLabel, BorderLayout.NORTH);

        // ② カレンダーパネル
        JPanel calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.setBackground(new Color(237,231,246));//薄パープル
        
        calendar = new JCalendar();
        calendar.setBackground(new Color(237,231,246));//薄パープル
        calendar.getDayChooser().setBackground(new Color(237,231,246)); // 日付部分の背景色//薄パープル
        calendar.getMonthChooser().setBackground(new Color(237,231,246)); // 月選択の背景色//薄パープル
        calendar.getYearChooser().setBackground(new Color(237,231,246)); // 年選択の背景色//薄パープル
        
        
        calendarPanel.add(calendar, BorderLayout.CENTER);
        

        selectedDateLabel = new JLabel("選択日: ");
        selectedDateLabel.setFont(new Font("Serif", Font.BOLD, 14));
        calendarPanel.add(selectedDateLabel, BorderLayout.NORTH);

        diaryDisplay = new JTextArea(5, 40);
        diaryDisplay.setEditable(false);
        diaryDisplay.setBackground(new Color(252,228,236));
        diaryDisplay.setForeground(Color.BLACK);
        calendarPanel.add(new JScrollPane(diaryDisplay), BorderLayout.SOUTH);

        calendar.addPropertyChangeListener("calendar", e -> loadDiaryForSelectedDate());
        
        customizeCalendar();
        
     // コレクション一覧タブ
       JPanel collectionPanel = new JPanel(new BorderLayout());
       collectionItemPanel = new JPanel(); // アイテムを追加するパネル
 //      collectionItemPanel.setPreferredSize(new Dimension(400, 300)); // 適当なサイズ

       
       collectionItemPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 行数は自動（0）、2列、水平間隔10、垂直間隔10
       collectionItemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // パネルの余白を設定

       scrollPane.setPreferredSize(new Dimension(380, 120)); // 横幅380、縦幅120
       scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // スクロールバーを常に表示
        
  //      JScrollPane collectionScrollPane = new JScrollPane(collectionItemPanel);
        collectionPanel.add(new JScrollPane(collectionItemPanel), BorderLayout.CENTER);
        
 //       frame.add(ScrollPane, BorderLayout.CENTER);

        itemLabel = new JLabel("✿ アイテム ✿");  
        collectionPanel.add(itemLabel, BorderLayout.NORTH);
        
        loadCollection();
//        updateCollectionDisplay();

        tabbedPane.addTab("✎　日記を書く　✎", diaryPanel);
        tabbedPane.addTab("☽  カレンダー　☽", calendarPanel);
        tabbedPane.addTab("🎁 コレクション 🎁", collectionPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);

        loadDiary();
        updateCollectionDisplay();
        
    }
    
    private static final String[] ITEMS = {"カレーライス", "フルーツサンド", "お弁当", "お茶菓子","梅おにぎり","わかめスープ","生ドーナッツ","トゥンカロン","ごぼ天うどん"};
    private static final String[] ITEM_IMAGES = {
    		"C:\\Users\\dnst3\\eclipse-workspace\\AppDiary\\src\\main\\resources\\images\\curry_rice.png", 
    		"C:\\Users\\dnst3\\eclipse-workspace\\AppDiary\\src\\main\\resources\\images\\fruit_sandwich_ichigo.png", 
    		"C:\\Users\\dnst3\\eclipse-workspace\\AppDiary\\src\\main\\resources\\images\\obentou_magewappa.png", 
    		"C:\\Users\\dnst3\\eclipse-workspace\\AppDiary\\src\\main\\resources\\images\\ochagashi.png",
    		"C:\\Users\\dnst3\\eclipse-workspace\\AppDiary\\src\\main\\resources\\images\\onigiri_ume.png",
    		"C:\\Users\\dnst3\\eclipse-workspace\\AppDiary\\src\\main\\resources\\images\\soup_wakame.png",
    		"C:\\Users\\dnst3\\eclipse-workspace\\AppDiary\\src\\main\\resources\\images\\sweets_nama_donuts_cherry.png",
    		"C:\\Users\\dnst3\\eclipse-workspace\\AppDiary\\src\\main\\resources\\images\\sweets_tuncaron.png",
    		"C:\\Users\\dnst3\\eclipse-workspace\\AppDiary\\src\\main\\resources\\images\\udon_goboten.png",
    };
    
    private static void customizeCalendar() {
    	
    
        JDayChooser dayChooser = calendar.getDayChooser();
        dayChooser.setWeekOfYearVisible(false); // 週番号の表示を非表示にする
    }


    private static void saveDiary() { 
        try {
            String date = getCurrentDate();
            File file = new File(DIARY_FOLDER + "/" + date + ".txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(diaryText.getText());
            writer.close();


            JOptionPane.showMessageDialog(null, "日記を保存しました！");

            
            // catLabel が null でないことを確認
            if (catLabel != null) {
           
                SwingUtilities.invokeLater(() -> {
                    catLabel.setIcon(new ImageIcon("C:\\Users\\dnst3\\eclipse-workspace\\AppDiary\\src\\main\\resources\\images\\image.cat3.gif"));
                    catLabel.revalidate();
                    catLabel.repaint();
                });
             
             
                
                
                // 元のGIFに戻す
                Timer timer = new Timer(3350, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        catLabel.setIcon(new ImageIcon("C:\\Users\\dnst3\\eclipse-workspace\\AppDiary\\src\\main\\resources\\images\\image.cat4.gif"));
                    }
                    
                 
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                System.err.println("catLabel が null です。初期化を確認してください。");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
 
    
    
 // 獲得アイテムをコレクションに追加するメソッド
    private static void addToCollection(String itemName, String itemImagePath) {
    	if (itemName != null && itemImagePath != null) {
        collectedItems.add(itemName); // リストに追加
        collectedImages.add(itemImagePath);
        SwingUtilities.invokeLater(() ->updateCollectionDisplay()); // コレクションを即時更新
    }else {
        System.err.println("エラー: null のアイテムまたは画像パスを追加しようとしました");
    }
    }

 // コレクションを更新するメソッド（画像＋テキストを表示）
    private static void updateCollectionDisplay() {
    	if (collectionItemPanel == null) {
            System.err.println("collectionItemPanel is null! Please initialize it.");
            return; // 処理を終了
        }

        collectionItemPanel.removeAll(); // クリアしてから追加
        
     // ★ リストが空なら何もしない
        if (collectedItems.isEmpty() || collectedImages.isEmpty()) {
            collectionItemPanel.revalidate();
            collectionItemPanel.repaint();
            return;
        }
        
        System.out.println("updateCollectionDisplay() 呼び出し時の collectedImages.size(): " + collectedImages.size());

        	for (int i = 0; i < collectedItems.size(); i++) {
        	
        	if (i >= 0 && i < collectedImages.size()) {
        		
        	
           String itemName = collectedItems.get(i);
            String itemImagePath = collectedImages.get(i);
            

           
               


        	
            // アイテム画像
 //          JLabel imageLabel = new JLabel(new ImageIcon(itemImagePath));
 //           imageLabel.setPreferredSize(new Dimension(50, 50)); // 画像のサイズを指定

 //           String itemName = "Item " + (i + 1);  // 例としてアイテム名を設定（適切な名前付けに変更）

            // ここでアイテム画像と名前を使って処理を行う
            System.out.println("画像パス: " + itemImagePath + ", アイテム名: " + itemName);
            
            JLabel imageLabel = new JLabel(new ImageIcon(itemImagePath));
            // アイテム名
            JLabel nameLabel = new JLabel("「" + itemName + "」");

            // アイテムを表示するパネル
            JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            itemPanel.add(imageLabel);
            itemPanel.add(nameLabel);

            collectionItemPanel.add(itemPanel); // コレクションパネルに追加
        	} else {
                System.out.println("Index " + i + " is out of bounds. collectedImages.size(): " + collectedImages.size());
            }
        }

        collectionItemPanel.revalidate(); // レイアウトを更新
        collectionItemPanel.repaint(); // 再描画
    }

    
 // アイテムをランダムに獲得するタイマーの設定
    private static void startPopupTimer() {
    Random random = new Random();
    int itemIndex = random.nextInt(ITEMS.length);

    

    // 3秒後にポップアップを表示する
    Timer popupTimer = new Timer(3340, e -> {
        showRewardPopup(ITEMS[itemIndex], ITEM_IMAGES[itemIndex]);
        // コレクションにアイテムを追加
    //    addToCollection(ITEMS[itemIndex], ITEM_IMAGES[itemIndex]);
    });
    popupTimer.setRepeats(false); // 1回だけ実行
    popupTimer.start();
    
    }
    

     // 獲得ポップアップを表示するメソッド
    private static void showRewardPopup(String itemName, String itemImagePath) {
        JDialog dialog = new JDialog();
        dialog.setUndecorated(true); // 枠なし
        dialog.setLayout(new BorderLayout());
        
        // 画像のラベル
        JLabel imgLabel = new JLabel(new ImageIcon(itemImagePath));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // テキストのラベル        
        JLabel textLabel = new JLabel("「" + itemName + "」を獲得しました！");        
        textLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // パネルに追加
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(imgLabel, BorderLayout.CENTER);
        panel.add(textLabel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.pack();
    
        // 画面中央に表示
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        // 一定時間（3.35秒）後に消す
        Timer timer = new Timer(2000, e -> {
        dialog.dispose();
        
        });
        timer.setRepeats(false);
        timer.start();
        
     // ここでアイテムをコレクションに追加する
        addToCollection(itemName, itemImagePath);
    }
        
        
 

    private static void loadDiary() {
        try {
            String date = getCurrentDate();
            File file = new File(DIARY_FOLDER + "/" + date + ".txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                diaryText.setText("");
                String line;
                while ((line = reader.readLine()) != null) {
                    diaryText.append(line + "\n");
                }
                reader.close();
            } else {
                diaryText.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadDiaryForSelectedDate() {
        try {
            String date = getSelectedDate();
            selectedDateLabel.setText("選択日: " + date);
            File file = new File(DIARY_FOLDER + "/" + date + ".txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                diaryDisplay.setText("");
                String line;
                while ((line = reader.readLine()) != null) {
                    diaryDisplay.append(line + "\n");
                }
                reader.close();
            } else {
                diaryDisplay.setText("この日に日記はありません。");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getSelectedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getDate());
    }

    private static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
    
    private static String collectRandomItem() {
        Random random = new Random();
        return ITEMS[random.nextInt(ITEMS.length)];
    }
    
 // アイテム画像の更新
//    private static void updateItemImage(String item) {
//        int index = Arrays.asList(ITEMS).indexOf(item);
//        if (index != -1) {
//            ImageIcon itemImage = new ImageIcon(ITEM_IMAGES[index]);
//            Image img = itemImage.getImage();
//            Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // 画像をスケーリング
//            itemLabel.setIcon(new ImageIcon(scaledImg)); // 画像をラベルに設定
//        }
//    }



    private static void saveCollection() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("collection.txt"))) {
            for (String item : collectedItems) {
                writer.write(item);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void loadCollection() {
        File file = new File("collection.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    collectedItems.add(line);
                }
                if (collectedItems.isEmpty() || collectedImages.isEmpty()) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




