package featurea.app;

import featurea.opengl.Texture;
import featurea.xml.XmlContext;
import featurea.xml.XmlEditor;
import featurea.xml.XmlResource;
import featurea.xml.XmlTag;

import java.io.File;

public class XmlResources {

  public final Project project;
  public XmlEditor editor;

  public XmlResources(Project project) {
    this.project = project;
  }

  public XmlContext getContext(Class klass) {
    return getContext(getIdByClass(klass));
  }

  public XmlContext getContext(File file) {
    return getContext(getIdByFile(file));
  }

  public XmlContext getContext(String id) {
    return new XmlContext(this, id);
  }

  public XmlTag getTag(Class klass) {
    return getTag(getIdByClass(klass));
  }

  public XmlTag getTag(File file) {
    return getTag(getIdByFile(file));
  }

  public XmlTag getTag(String id) {
    XmlContext context = getContext(id);
    return context.xmlTag;
  }

  public <T extends XmlResource> T getResource(Class klass) {
    return getResource(getIdByClass(klass));
  }

  public <T extends XmlResource> T getResource(File file) {
    return getResource(getIdByFile(file));
  }

  public <T extends XmlResource> T getResource(String id) {
    XmlTag xmlTag = getContext(id).xmlTag;
    return xmlTag.getResource();
  }

  private static String getIdByClass(Class klass) {
    return "/" + klass.getCanonicalName().replaceAll("res\\.", "").replaceAll("\\.", "/").replaceAll("\\$", "/");
  }

  public String getIdByFile(File file) {
    try {
      String relativePath = project.getFiles().getRelativePath(file.getAbsolutePath());
      return "/" + relativePath.replaceAll(".xml", "");
    } catch (NullPointerException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Texture getTexture(String file) {
    return Context.getRender().getTextureManager().getTexture(file);
  }

  public Iterable<Texture> getTextures() {
    return Context.getRender().getTextureManager().getTextures();
  }

}
