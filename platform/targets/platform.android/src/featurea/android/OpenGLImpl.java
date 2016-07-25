package featurea.android;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.os.Build;
import featurea.app.Context;
import featurea.app.MediaPlayer;
import featurea.opengl.OpenGL;
import featurea.opengl.OpenGLManager;
import featurea.util.Size;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class OpenGLImpl extends OpenGLManager {

  @Override
  public Size loadTexture(String file) {
    Bitmap bitmap = BitmapFactory.decodeStream(Context.getFiles().getStream(file));
    Size size = new Size(bitmap.getWidth(), bitmap.getHeight());
    if (isPOT((int) size.width) && isPOT((int) size.height)) {
      GLUtils.texImage2D(OpenGL.GL_TEXTURE_2D, 0, bitmap, 0);
      bitmap.recycle();
      return size;
    }
    throw new RuntimeException("Texture size is not power of two");
  }

  @Override
  public IntBuffer getPixels(int textureId, Size size) {
    throw new RuntimeException("Not implemented yet");
  }

  private static boolean isPOT(int x) {
    return (x & (x - 1)) == 0;
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glActiveTexture(int texture) {
    android.opengl.GLES11.glActiveTexture(texture);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glAlphaFunc(int func, float ref) {
    android.opengl.GLES11.glAlphaFunc(func, ref);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glAlphaFuncx(int func, int ref) {
    android.opengl.GLES11.glAlphaFuncx(func, ref);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glBindTexture(int target, int texture) {
    android.opengl.GLES11.glBindTexture(target, texture);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glBlendFunc(int sfactor, int dfactor) {
    android.opengl.GLES11.glBlendFunc(sfactor, dfactor);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glClear(int mask) {
    android.opengl.GLES11.glClear(mask);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glClearColor(float red, float green, float blue, float alpha) {
    android.opengl.GLES11.glClearColor(red, green, blue, alpha);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glClearColorx(int red, int green, int blue, int alpha) {
    android.opengl.GLES11.glClearColorx(red, green, blue, alpha);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glClearDepthf(float depth) {
    android.opengl.GLES11.glClearDepthf(depth);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glClearDepthx(int depth) {
    android.opengl.GLES11.glClearDepthx(depth);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glClearStencil(int s) {
    android.opengl.GLES11.glClearStencil(s);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glClientActiveTexture(int texture) {
    android.opengl.GLES11.glClientActiveTexture(texture);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glColor4f(float red, float green, float blue, float alpha) {
    android.opengl.GLES11.glColor4f(red, green, blue, alpha);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glColor4x(int red, int green, int blue, int alpha) {
    android.opengl.GLES11.glColor4x(red, green, blue, alpha);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
    android.opengl.GLES11.glColorMask(red, green, blue, alpha);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glColorPointer(int size, int type, int stride, Buffer pointer) {
    android.opengl.GLES11.glColorPointer(size, type, stride, pointer);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize, Buffer data) {
    android.opengl.GLES11.glCompressedTexImage2D(target, level, internalformat, width, height, border, imageSize, data);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, Buffer data) {
    android.opengl.GLES11.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, imageSize, data);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border) {
    android.opengl.GLES11.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
    android.opengl.GLES11.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glCullFace(int mode) {
    android.opengl.GLES11.glCullFace(mode);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glDeleteTextures(int n, int[] textures, int offset) {
    android.opengl.GLES11.glDeleteTextures(n, textures, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glDeleteTextures(int n, IntBuffer textures) {
    android.opengl.GLES11.glDeleteTextures(n, textures);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glDepthFunc(int func) {
    android.opengl.GLES11.glDepthFunc(func);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glDepthMask(boolean flag) {
    android.opengl.GLES11.glDepthMask(flag);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glDepthRangef(float zNear, float zFar) {
    android.opengl.GLES11.glDepthRangef(zNear, zFar);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glDepthRangex(int zNear, int zFar) {
    android.opengl.GLES11.glDepthRangex(zNear, zFar);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glDisable(int cap) {
    android.opengl.GLES11.glDisable(cap);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glDisableClientState(int array) {
    android.opengl.GLES11.glDisableClientState(array);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glDrawArrays(int mode, int first, int count) {
    android.opengl.GLES11.glDrawArrays(mode, first, count);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glDrawElements(int mode, int count, int type, Buffer indices) {
    android.opengl.GLES11.glDrawElements(mode, count, type, indices);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glEnable(int cap) {
    android.opengl.GLES11.glEnable(cap);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glEnableClientState(int array) {
    android.opengl.GLES11.glEnableClientState(array);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glFinish() {
    android.opengl.GLES11.glFinish();
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glFlush() {
    android.opengl.GLES11.glFlush();
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glFogf(int pname, float param) {
    android.opengl.GLES11.glFogf(pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glFogfv(int pname, float[] params, int offset) {
    android.opengl.GLES11.glFogfv(pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glFogfv(int pname, FloatBuffer params) {
    android.opengl.GLES11.glFogfv(pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glFogx(int pname, int param) {
    android.opengl.GLES11.glFogx(pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glFogxv(int pname, int[] params, int offset) {
    android.opengl.GLES11.glFogxv(pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glFogxv(int pname, IntBuffer params) {
    android.opengl.GLES11.glFogxv(pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glFrontFace(int mode) {
    android.opengl.GLES11.glFrontFace(mode);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glFrustumf(float left, float right, float bottom, float top, float zNear, float zFar) {
    android.opengl.GLES11.glFrustumf(left, right, bottom, top, zNear, zFar);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glFrustumx(int left, int right, int bottom, int top, int zNear, int zFar) {
    android.opengl.GLES11.glFrustumx(left, right, bottom, top, zNear, zFar);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGenTextures(int n, int[] textures, int offset) {
    android.opengl.GLES11.glGenTextures(n, textures, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGenTextures(int n, IntBuffer textures) {
    android.opengl.GLES11.glGenTextures(n, textures);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public int glGetError() {
    return android.opengl.GLES11.glGetError();
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetIntegerv(int pname, int[] params, int offset) {
    android.opengl.GLES11.glGetIntegerv(pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetIntegerv(int pname, IntBuffer params) {
    android.opengl.GLES11.glGetIntegerv(pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public String glGetString(int name) {
    return android.opengl.GLES11.glGetString(name);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glHint(int target, int mode) {
    android.opengl.GLES11.glHint(target, mode);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLightModelf(int pname, float param) {
    android.opengl.GLES11.glLightModelf(pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLightModelfv(int pname, float[] params, int offset) {
    android.opengl.GLES11.glLightModelfv(pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLightModelfv(int pname, FloatBuffer params) {
    android.opengl.GLES11.glLightModelfv(pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLightModelx(int pname, int param) {
    android.opengl.GLES11.glLightModelx(pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLightModelxv(int pname, int[] params, int offset) {
    android.opengl.GLES11.glLightModelxv(pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLightModelxv(int pname, IntBuffer params) {
    android.opengl.GLES11.glLightModelxv(pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLightf(int light, int pname, float param) {
    android.opengl.GLES11.glLightf(light, pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLightfv(int light, int pname, float[] params, int offset) {
    android.opengl.GLES11.glLightfv(light, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLightfv(int light, int pname, FloatBuffer params) {
    android.opengl.GLES11.glLightfv(light, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLightx(int light, int pname, int param) {
    android.opengl.GLES11.glLightx(light, pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLightxv(int light, int pname, int[] params, int offset) {
    android.opengl.GLES11.glLightxv(light, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLightxv(int light, int pname, IntBuffer params) {
    android.opengl.GLES11.glLightxv(light, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLineWidth(float width) {
    android.opengl.GLES11.glLineWidth(width);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLineWidthx(int width) {
    android.opengl.GLES11.glLineWidthx(width);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLoadIdentity() {
    android.opengl.GLES11.glLoadIdentity();
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLoadMatrixf(float[] m, int offset) {
    android.opengl.GLES11.glLoadMatrixf(m, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLoadMatrixf(FloatBuffer m) {
    android.opengl.GLES11.glLoadMatrixf(m);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLoadMatrixx(int[] m, int offset) {
    android.opengl.GLES11.glLoadMatrixx(m, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLoadMatrixx(IntBuffer m) {
    android.opengl.GLES11.glLoadMatrixx(m);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glLogicOp(int opcode) {
    android.opengl.GLES11.glLogicOp(opcode);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glMaterialf(int face, int pname, float param) {
    android.opengl.GLES11.glMaterialf(face, pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glMaterialfv(int face, int pname, float[] params, int offset) {
    android.opengl.GLES11.glMaterialfv(face, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glMaterialfv(int face, int pname, FloatBuffer params) {
    android.opengl.GLES11.glMaterialfv(face, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glMaterialx(int face, int pname, int param) {
    android.opengl.GLES11.glMaterialx(face, pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glMaterialxv(int face, int pname, int[] params, int offset) {
    android.opengl.GLES11.glMaterialxv(face, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glMaterialxv(int face, int pname, IntBuffer params) {
    android.opengl.GLES11.glMaterialxv(face, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glMatrixMode(int mode) {
    android.opengl.GLES11.glMatrixMode(mode);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glMultMatrixf(float[] m, int offset) {
    android.opengl.GLES11.glMultMatrixf(m, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glMultMatrixf(FloatBuffer m) {
    android.opengl.GLES11.glMultMatrixf(m);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glMultMatrixx(int[] m, int offset) {
    android.opengl.GLES11.glMultMatrixx(m, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glMultMatrixx(IntBuffer m) {
    android.opengl.GLES11.glMultMatrixx(m);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glMultiTexCoord4f(int target, float s, float t, float r, float q) {
    android.opengl.GLES11.glMultiTexCoord4f(target, s, t, r, q);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glMultiTexCoord4x(int target, int s, int t, int r, int q) {
    android.opengl.GLES11.glMultiTexCoord4x(target, s, t, r, q);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glNormal3f(float nx, float ny, float nz) {
    android.opengl.GLES11.glNormal3f(nx, ny, nz);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glNormal3x(int nx, int ny, int nz) {
    android.opengl.GLES11.glNormal3x(nx, ny, nz);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glNormalPointer(int type, int stride, Buffer pointer) {
    android.opengl.GLES11.glNormalPointer(type, stride, pointer);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glOrthof(float left, float right, float bottom, float top, float zNear, float zFar) {
    android.opengl.GLES11.glOrthof(left, right, bottom, top, zNear, zFar);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glOrthox(int left, int right, int bottom, int top, int zNear, int zFar) {
    android.opengl.GLES11.glOrthox(left, right, bottom, top, zNear, zFar);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glPixelStorei(int pname, int param) {
    android.opengl.GLES11.glPixelStorei(pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glPointSize(float size) {
    android.opengl.GLES11.glPointSize(size);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glPointSizex(int size) {
    android.opengl.GLES11.glPointSizex(size);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glPolygonOffset(float factor, float units) {
    android.opengl.GLES11.glPolygonOffset(factor, units);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glPolygonOffsetx(int factor, int units) {
    android.opengl.GLES11.glPolygonOffsetx(factor, units);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glPopMatrix() {
    android.opengl.GLES11.glPopMatrix();
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glPushMatrix() {
    android.opengl.GLES11.glPushMatrix();
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glReadPixels(int x, int y, int width, int height, int format, int type, Buffer pixels) {
    android.opengl.GLES11.glReadPixels(x, y, width, height, format, type, pixels);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glRotatef(float angle, float x, float y, float z) {
    android.opengl.GLES11.glRotatef(angle, x, y, z);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glRotatex(int angle, int x, int y, int z) {
    android.opengl.GLES11.glRotatex(angle, x, y, z);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glSampleCoverage(float value, boolean invert) {
    android.opengl.GLES11.glSampleCoverage(value, invert);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glSampleCoveragex(int value, boolean invert) {
    android.opengl.GLES11.glSampleCoveragex(value, invert);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glScalef(float x, float y, float z) {
    android.opengl.GLES11.glScalef(x, y, z);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glScalex(int x, int y, int z) {
    android.opengl.GLES11.glScalex(x, y, z);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glScissor(int x, int y, int width, int height) {
    android.opengl.GLES11.glScissor(x, y, width, height);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glShadeModel(int mode) {
    android.opengl.GLES11.glShadeModel(mode);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glStencilFunc(int func, int ref, int mask) {
    android.opengl.GLES11.glStencilFunc(func, ref, mask);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glStencilMask(int mask) {
    android.opengl.GLES11.glStencilMask(mask);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glStencilOp(int fail, int zfail, int zpass) {
    android.opengl.GLES11.glStencilOp(fail, zfail, zpass);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexCoordPointer(int size, int type, int stride, Buffer pointer) {
    android.opengl.GLES11.glTexCoordPointer(size, type, stride, pointer);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexEnvf(int target, int pname, float param) {
    android.opengl.GLES11.glTexEnvf(target, pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexEnvfv(int target, int pname, float[] params, int offset) {
    android.opengl.GLES11.glTexEnvfv(target, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexEnvfv(int target, int pname, FloatBuffer params) {
    android.opengl.GLES11.glTexEnvfv(target, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexEnvx(int target, int pname, int param) {
    android.opengl.GLES11.glTexEnvx(target, pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexEnvxv(int target, int pname, int[] params, int offset) {
    android.opengl.GLES11.glTexEnvxv(target, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexEnvxv(int target, int pname, IntBuffer params) {
    android.opengl.GLES11.glTexEnvxv(target, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels) {
    android.opengl.GLES11.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexParameterf(int target, int pname, float param) {
    android.opengl.GLES11.glTexParameterf(target, pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexParameterx(int target, int pname, int param) {
    android.opengl.GLES11.glTexParameterx(target, pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels) {
    android.opengl.GLES11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTranslatef(float x, float y, float z) {
    android.opengl.GLES11.glTranslatef(x, y, z);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTranslatex(int x, int y, int z) {
    android.opengl.GLES11.glTranslatex(x, y, z);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glVertexPointer(int size, int type, int stride, Buffer pointer) {
    android.opengl.GLES11.glVertexPointer(size, type, stride, pointer);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glViewport(int x, int y, int width, int height) {
    android.opengl.GLES11.glViewport(x, y, width, height);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glBindBuffer(int target, int buffer) {
    android.opengl.GLES11.glBindBuffer(target, buffer);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glBufferData(int target, int size, Buffer data, int usage) {
    android.opengl.GLES11.glBufferData(target, size, data, usage);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glBufferSubData(int target, int offset, int size, Buffer data) {
    android.opengl.GLES11.glBufferSubData(target, offset, size, data);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glClipPlanef(int plane, float[] equation, int offset) {
    android.opengl.GLES11.glClipPlanef(plane, equation, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glClipPlanef(int plane, FloatBuffer equation) {
    android.opengl.GLES11.glClipPlanef(plane, equation);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glClipPlanex(int plane, int[] equation, int offset) {
    android.opengl.GLES11.glClipPlanex(plane, equation, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glClipPlanex(int plane, IntBuffer equation) {
    android.opengl.GLES11.glClipPlanex(plane, equation);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glColor4ub(byte red, byte green, byte blue, byte alpha) {
    android.opengl.GLES11.glColor4ub(red, green, blue, alpha);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glColorPointer(int size, int type, int stride, int offset) {
    android.opengl.GLES11.glColorPointer(size, type, stride, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glDeleteBuffers(int n, int[] buffers, int offset) {
    android.opengl.GLES11.glDeleteBuffers(n, buffers, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glDeleteBuffers(int n, IntBuffer buffers) {
    android.opengl.GLES11.glDeleteBuffers(n, buffers);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glDrawElements(int mode, int count, int type, int offset) {
    android.opengl.GLES11.glDrawElements(mode, count, type, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGenBuffers(int n, int[] buffers, int offset) {
    android.opengl.GLES11.glGenBuffers(n, buffers, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGenBuffers(int n, IntBuffer buffers) {
    android.opengl.GLES11.glGenBuffers(n, buffers);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetBooleanv(int pname, boolean[] params, int offset) {
    android.opengl.GLES11.glGetBooleanv(pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetBooleanv(int pname, IntBuffer params) {
    android.opengl.GLES11.glGetBooleanv(pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetBufferParameteriv(int target, int pname, int[] params, int offset) {
    android.opengl.GLES11.glGetBufferParameteriv(target, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetBufferParameteriv(int target, int pname, IntBuffer params) {
    android.opengl.GLES11.glGetBufferParameteriv(target, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetClipPlanef(int pname, float[] eqn, int offset) {
    android.opengl.GLES11.glGetClipPlanef(pname, eqn, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetClipPlanef(int pname, FloatBuffer eqn) {
    android.opengl.GLES11.glGetClipPlanef(pname, eqn);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetClipPlanex(int pname, int[] eqn, int offset) {
    android.opengl.GLES11.glGetClipPlanex(pname, eqn, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetClipPlanex(int pname, IntBuffer eqn) {
    android.opengl.GLES11.glGetClipPlanex(pname, eqn);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetFixedv(int pname, int[] params, int offset) {
    android.opengl.GLES11.glGetFixedv(pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetFixedv(int pname, IntBuffer params) {
    android.opengl.GLES11.glGetFixedv(pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetFloatv(int pname, float[] params, int offset) {
    android.opengl.GLES11.glGetFloatv(pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetFloatv(int pname, FloatBuffer params) {
    android.opengl.GLES11.glGetFloatv(pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetLightfv(int light, int pname, float[] params, int offset) {
    android.opengl.GLES11.glGetLightfv(light, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetLightfv(int light, int pname, FloatBuffer params) {
    android.opengl.GLES11.glGetLightfv(light, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetLightxv(int light, int pname, int[] params, int offset) {
    android.opengl.GLES11.glGetLightxv(light, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetLightxv(int light, int pname, IntBuffer params) {
    android.opengl.GLES11.glGetLightxv(light, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetMaterialfv(int face, int pname, float[] params, int offset) {
    android.opengl.GLES11.glGetMaterialfv(face, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetMaterialfv(int face, int pname, FloatBuffer params) {
    android.opengl.GLES11.glGetMaterialfv(face, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetMaterialxv(int face, int pname, int[] params, int offset) {
    android.opengl.GLES11.glGetMaterialxv(face, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetMaterialxv(int face, int pname, IntBuffer params) {
    android.opengl.GLES11.glGetMaterialxv(face, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetTexEnvfv(int env, int pname, float[] params, int offset) {
    android.opengl.GLES11.glGetTexEnvfv(env, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetTexEnvfv(int env, int pname, FloatBuffer params) {
    android.opengl.GLES11.glGetTexEnvfv(env, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetTexEnviv(int env, int pname, int[] params, int offset) {
    android.opengl.GLES11.glGetTexEnviv(env, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetTexEnviv(int env, int pname, IntBuffer params) {
    android.opengl.GLES11.glGetTexEnviv(env, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetTexEnvxv(int env, int pname, int[] params, int offset) {
    android.opengl.GLES11.glGetTexEnviv(env, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetTexEnvxv(int env, int pname, IntBuffer params) {
    android.opengl.GLES11.glGetTexEnvxv(env, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetTexParameterfv(int target, int pname, float[] params, int offset) {
    android.opengl.GLES11.glGetTexParameterfv(target, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetTexParameterfv(int target, int pname, FloatBuffer params) {
    android.opengl.GLES11.glGetTexParameterfv(target, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetTexParameteriv(int target, int pname, int[] params, int offset) {
    android.opengl.GLES11.glGetTexParameteriv(target, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetTexParameteriv(int target, int pname, IntBuffer params) {
    android.opengl.GLES11.glGetTexParameteriv(target, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetTexParameterxv(int target, int pname, int[] params, int offset) {
    android.opengl.GLES11.glGetTexParameterxv(target, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glGetTexParameterxv(int target, int pname, IntBuffer params) {
    android.opengl.GLES11.glGetTexParameterxv(target, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public boolean glIsBuffer(int buffer) {
    return android.opengl.GLES11.glIsBuffer(buffer);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public boolean glIsEnabled(int cap) {
    return android.opengl.GLES11.glIsEnabled(cap);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public boolean glIsTexture(int texture) {
    return android.opengl.GLES11.glIsTexture(texture);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glNormalPointer(int type, int stride, int offset) {
    android.opengl.GLES11.glNormalPointer(type, stride, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glPointParameterf(int pname, float param) {
    android.opengl.GLES11.glPointParameterf(pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glPointParameterfv(int pname, float[] params, int offset) {
    android.opengl.GLES11.glPointParameterfv(pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glPointParameterfv(int pname, FloatBuffer params) {
    android.opengl.GLES11.glPointParameterfv(pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glPointParameterx(int pname, int param) {
    android.opengl.GLES11.glPointParameterx(pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glPointParameterxv(int pname, int[] params, int offset) {
    android.opengl.GLES11.glPointParameterxv(pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glPointParameterxv(int pname, IntBuffer params) {
    android.opengl.GLES11.glPointParameterxv(pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glPointSizePointerOES(int type, int stride, Buffer pointer) {
    android.opengl.GLES11.glPointSizePointerOES(type, stride, pointer);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexCoordPointer(int size, int type, int stride, int offset) {
    android.opengl.GLES11.glTexCoordPointer(size, type, stride, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexEnvi(int target, int pname, int param) {
    android.opengl.GLES11.glTexEnvi(target, pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexEnviv(int target, int pname, int[] params, int offset) {
    android.opengl.GLES11.glTexEnviv(target, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexEnviv(int target, int pname, IntBuffer params) {
    android.opengl.GLES11.glTexEnviv(target, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexParameterfv(int target, int pname, float[] params, int offset) {
    android.opengl.GLES11.glTexParameterfv(target, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexParameterfv(int target, int pname, FloatBuffer params) {
    android.opengl.GLES11.glTexParameterfv(target, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexParameteri(int target, int pname, int param) {
    android.opengl.GLES11.glTexParameteri(target, pname, param);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexParameteriv(int target, int pname, int[] params, int offset) {
    android.opengl.GLES11.glTexParameteriv(target, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexParameteriv(int target, int pname, IntBuffer params) {
    android.opengl.GLES11.glTexParameteriv(target, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexParameterxv(int target, int pname, int[] params, int offset) {
    android.opengl.GLES11.glTexParameterxv(target, pname, params, offset);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glTexParameterxv(int target, int pname, IntBuffer params) {
    android.opengl.GLES11.glTexParameterxv(target, pname, params);
  }

  @TargetApi(Build.VERSION_CODES.DONUT)
  @Override
  public void glVertexPointer(int size, int type, int stride, int offset) {
    android.opengl.GLES11.glVertexPointer(size, type, stride, offset);
  }
}
