package featurea.xmlEditor.views;

import featurea.app.MediaPlayer;
import featurea.swing.FeatureaSwingUtil;
import featurea.swing.MyTable;
import featurea.util.ArrayList;
import featurea.app.Config;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class AreasView extends MyTable {

  private String filterText = "";
  private final List<Object[]> data = new ArrayList<>();
  private AreaSuggestionView.ChooseAreaListener chooseListener;

  public AreasView(MediaPlayer mediaPlayer) {
    super(mediaPlayer);
    isAutoResize = false;
    setShowGrid(false);
    setTableHeader(null);
    setModel(new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    });
    setRowHeight(34);
    setColumnWidth(0, 38);
  }

  public void update() {
    data.clear();
    Config config = new Config(mediaPlayer.project, "areaSuggestion.properties");

    for (Map.Entry entry : config.entrySet()) {
      String key = entry.getKey().toString();
      if (key.toLowerCase().startsWith(filterText.toLowerCase())) {
        data.add(new Object[]{entry.getValue(), key});
      }
    }
    setData(data.toArray(new Object[data.size()][2]), new Object[]{"Icon", "Area"});

    // image inside table support
    getColumnModel().getColumn(0).setCellRenderer(new TableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        String file = (String) value;
        BufferedImage image = FeatureaSwingUtil.getBufferedImage(file);
        try {
          label.setIcon(new ImageIcon(image));
        } catch (NullPointerException e) {
          e.printStackTrace();
        }
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
      }
    });

    addKeyListener(new KeyAdapter() {
      @Override
      public synchronized void keyPressed(KeyEvent e) {
        /*Context.targetContexts.put(Thread.currentThread(), context);*/
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          selectArea();
        }
      }
    });
    addMouseListener(new MouseAdapter() {
      @Override
      public synchronized void mouseClicked(MouseEvent e) {
        /*Context.targetContexts.put(Thread.currentThread(), context);*/
        if (e.getClickCount() == 2) {
          selectArea();
        }
      }
    });
  }

  private void selectArea() {
    int index = getSelectedRow();
    if (index != -1) {
      String area = (String) getValueAt(index, 1);
      chooseListener.choose(area);
    }
  }

  public void filter(String text) {
    this.filterText = text;
    update();
  }

  public void setChooseListener(AreaSuggestionView.ChooseAreaListener chooseListener) {
    this.chooseListener = chooseListener;
  }

  public AreaSuggestionView.ChooseAreaListener getChooseListener() {
    return chooseListener;
  }
}
