package com.badlogic.gdx.texturePacker;

import featurea.app.Project;
import featurea.util.FileUtil;
import featurea.util.Files;
import featurea.util.StringUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TexturePacker {

  private final Project project;
  private final Files resources;

  public TexturePacker(Project project) {
    this.project = project;
    resources = new Files();
    resources.addAll(project.getResourcesRoots());
  }

  public void pack() {
    long startTime = System.currentTimeMillis();
    performPack();
    long finishTime = System.currentTimeMillis();
    System.out.println("TexturePacker completed: " + (finishTime - startTime) / 1000 + " s");
  }

  private void performPack() {
    List<String> packedPngFiles = new ArrayList<>();
    packedPngFiles.addAll(packPngFiles(resources));
    copyPaste(packedPngFiles, resources);
  }

  private List<String> packPngFiles(Files resources) {
    List<String> processedFiles = new ArrayList<>();

    for (Map.Entry<String, String> entry : project.packProperties.properties.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      String[] inputDirs = StringUtil.split(value, ",");
      // >> hack
      if (inputDirs.length == 0) {
        inputDirs = new String[]{""};
      }
      // <<

      Map<String, List<String>> list = new HashMap<>();
      for (String inputDir : inputDirs) {
        String root = null;
        File file = resources.findFile(inputDir);
        if (file != null) {
          if (inputDir.isEmpty()) {
            root = file.getAbsolutePath();
          } else {
            root = file.getParentFile().getAbsolutePath();
          }
        }
        if (root != null) {
          root = FileUtil.formatPath(root);
          List<String> children = resources.getChildren(inputDir, ".png");
          putToMap(list, root, children);
        }
      }
      TexturePackerManager.Settings settings = new TexturePackerManager.Settings();
      TexturePackerManager texturePackerManager = new TexturePackerManager(settings);
      for (Map.Entry<String, List<String>> entry1 : list.entrySet()) {
        String key1 = entry1.getKey();
        List<String> value1 = entry1.getValue();
        for (String filePath : value1) {
          processedFiles.add(filePath); // IMPORTANT
          texturePackerManager.addImage(key1, resources.findFile(filePath), resources);
        }
      }
      texturePackerManager.pack(project.generatedFilesDir, key);
    }

    return processedFiles;
  }

  private void putToMap(Map<String, List<String>> list, String root, List<String> children) {
    List<String> files = list.get(root);
    if (files == null) {
      files = new ArrayList<>();
      list.put(root, files);
    }
    files.addAll(children);
  }

  private void copyPaste(List<String> packedPngFiles, Files resources) {
    List<String> allFiles = resources.getChildren("");
    for (String file : allFiles) {
      if (!packedPngFiles.contains(file) && !file.endsWith(".xml")) {
        try {
          File realFile = resources.findFile(file);
          File newFile = new File(project.generatedFilesDir, file);
          if (realFile.getAbsolutePath().equals(newFile.getAbsolutePath())) {
            throw new IllegalStateException("realFile == newFile");
          }
          FileUtil.copyPaste(new FileInputStream(realFile), newFile);
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }
    }
  }

}
