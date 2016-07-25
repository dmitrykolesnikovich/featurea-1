package featurea.desktop;

import featurea.app.MediaPlayer;
import featurea.opengl.OpenGLManager;
import featurea.swing.FeatureaSwingUtil;
import featurea.util.Size;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.*;

public class OpenGLImpl extends OpenGLManager {

  @Override
  public Size loadTexture(String file) {
    return FeatureaSwingUtil.loadTexture(file);
  }

  @Override
  public IntBuffer getPixels(int textureId, Size size) {
    return FeatureaSwingUtil.getPixels(textureId, (int) size.width, (int) size.height);
  }

  @Override
  public void glActiveTexture(int texture) {
    GL13.glActiveTexture(texture);
  }

  @Override
  public void glAlphaFunc(int func, float ref) {
    GL11.glAlphaFunc(func, ref);
  }

  @Override
  public void glAlphaFuncx(int func, int ref) {
    throw new UnsupportedOperationException("glAlphaFuncx");
  }

  @Override
  public void glBindTexture(int target, int texture) {
    GL11.glBindTexture(target, texture);
  }

  @Override
  public void glBlendFunc(int sfactor, int dfactor) {
    GL11.glBlendFunc(sfactor, dfactor);
  }

  @Override
  public void glClear(int mask) {
    GL11.glClear(mask);
  }

  @Override
  public void glClearColor(float red, float green, float blue, float alpha) {
    GL11.glClearColor(red, green, blue, alpha);
  }

  @Override
  public void glClearColorx(int red, int green, int blue, int alpha) {
    throw new UnsupportedOperationException("glClearColorx");
  }

  @Override
  public void glClearDepthf(float depth) {
    GL41.glClearDepthf(depth);
  }

  @Override
  public void glClearDepthx(int depth) {
    throw new UnsupportedOperationException("glClearDepthx");
  }

  @Override
  public void glClearStencil(int s) {
    GL11.glClearStencil(s);
  }

  @Override
  public void glClientActiveTexture(int texture) {
    GL13.glClientActiveTexture(texture);
  }

  @Override
  public void glColor4f(float red, float green, float blue, float alpha) {
    GL11.glColor4f(red, green, blue, alpha);
  }

  @Override
  public void glColor4x(int red, int green, int blue, int alpha) {
    throw new UnsupportedOperationException("glColor4x");
  }

  @Override
  public void glColorMask(boolean red, boolean green, boolean blue, boolean alpha) {
    GL11.glColorMask(red, green, blue, alpha);
  }

  @Override
  public void glColorPointer(int size, int type, int stride, Buffer pointer) {
    GL11.glColorPointer(size, stride, (FloatBuffer) pointer);
  }

  @Override
  public void glCompressedTexImage2D(int target, int level, int internalformat, int width, int height, int border, int imageSize, Buffer data) {
    GL13.glCompressedTexImage2D(target, level, internalformat, width, height, border, (ByteBuffer) data);
  }

  @Override
  public void glCompressedTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int imageSize, Buffer data) {
    GL13.glCompressedTexSubImage2D(target, level, xoffset, yoffset, width, height, format, (ByteBuffer) data);
  }

  @Override
  public void glCopyTexImage2D(int target, int level, int internalformat, int x, int y, int width, int height, int border) {
    GL11.glCopyTexImage2D(target, level, internalformat, x, y, width, height, border);
  }

  @Override
  public void glCopyTexSubImage2D(int target, int level, int xoffset, int yoffset, int x, int y, int width, int height) {
    GL11.glCopyTexSubImage2D(target, level, xoffset, yoffset, x, y, width, height);
  }

  @Override
  public void glCullFace(int mode) {
    GL11.glCullFace(mode);
  }

  @Override
  public void glDeleteTextures(int n, int[] textures, int offset) {
    IntBuffer buffer = (IntBuffer) BufferUtils.createIntBuffer(n).put(textures).position(offset);
    GL11.glDeleteTextures(buffer);
  }

  @Override
  public void glDeleteTextures(int n, IntBuffer textures) {
    GL11.glDeleteTextures(textures);
  }

  @Override
  public void glDepthFunc(int func) {
    GL11.glDepthFunc(func);
  }

  @Override
  public void glDepthMask(boolean flag) {
    GL11.glDepthMask(flag);
  }

  @Override
  public void glDepthRangef(float zNear, float zFar) {
    GL41.glDepthRangef(zNear, zFar);
  }

  @Override
  public void glDepthRangex(int zNear, int zFar) {
    GL41.glDepthRangef(zNear, zFar);
  }

  @Override
  public void glDisable(int cap) {
    GL11.glDisable(cap);
  }

  @Override
  public void glDisableClientState(int array) {
    GL11.glDisableClientState(array);
  }

  @Override
  public void glDrawArrays(int mode, int first, int count) {
    GL11.glDrawArrays(mode, first, count);
  }

  @Override
  public void glDrawElements(int mode, int count, int type, Buffer indices) {
    GL11.glDrawElements(mode, count, type, (ByteBuffer) indices);
  }

  @Override
  public void glEnable(int cap) {
    GL11.glEnable(cap);
  }

  @Override
  public void glEnableClientState(int array) {
    GL11.glEnableClientState(array);
  }

  @Override
  public void glFinish() {
    GL11.glFinish();
  }

  @Override
  public void glFlush() {
    GL11.glFlush();
  }

  @Override
  public void glFogf(int pname, float param) {
    GL11.glFogf(pname, param);
  }

  @Override
  public void glFogfv(int pname, float[] params, int offset) {
    GL11.glFogf(pname, params[offset]);
  }

  @Override
  public void glFogfv(int pname, FloatBuffer params) {
    GL11.glFog(pname, params);
  }

  @Override
  public void glFogx(int pname, int param) {
    GL11.glFogf(pname, param);
  }

  @Override
  public void glFogxv(int pname, int[] params, int offset) {
    GL11.glFogf(pname, params[offset]);
  }

  @Override
  public void glFogxv(int pname, IntBuffer params) {
    GL11.glFog(pname, params);
  }

  @Override
  public void glFrontFace(int mode) {
    GL11.glFrontFace(mode);
  }

  @Override
  public void glFrustumf(float left, float right, float bottom, float top, float zNear, float zFar) {
    GL11.glFrustum(left, right, bottom, top, zNear, zFar);
  }

  @Override
  public void glFrustumx(int left, int right, int bottom, int top, int zNear, int zFar) {
    GL11.glFrustum(left, right, bottom, top, zNear, zFar);
  }

  @Override
  public void glGenTextures(int n, int[] textures, int offset) {
    ByteBuffer temp = ByteBuffer.allocateDirect(4);
    temp.order(ByteOrder.nativeOrder());
    IntBuffer intBuffer = temp.asIntBuffer();
    intBuffer.put(textures[offset]).flip();
    GL11.glGenTextures(intBuffer);
  }

  @Override
  public void glGenTextures(int n, IntBuffer textures) {
    GL11.glGenTextures(textures);
  }

  @Override
  public int glGetError() {
    return GL11.glGetError();
  }

  @Override
  public void glGetIntegerv(int pname, int[] params, int offset) {
    IntBuffer buffer = (IntBuffer) BufferUtils.createIntBuffer(16).put(params).position(offset);
    GL11.glGetInteger(pname, buffer);
    for (int i = 0; i < params.length; i++) {
      params[i] = buffer.get(i);
    }
  }

  @Override
  public void glGetIntegerv(int pname, IntBuffer params) {
    GL11.glGetInteger(pname, params);
  }

  @Override
  public String glGetString(int name) {
    return GL11.glGetString(name);
  }

  @Override
  public void glHint(int target, int mode) {
    GL11.glHint(target, mode);
  }

  @Override
  public void glLightModelf(int pname, float param) {
    GL11.glLightModelf(pname, param);
  }

  @Override
  public void glLightModelfv(int pname, float[] params, int offset) {
    GL11.glLightModelf(pname, params[offset]);
  }

  @Override
  public void glLightModelfv(int pname, FloatBuffer params) {
    GL11.glLightModel(pname, params);
  }

  @Override
  public void glLightModelx(int pname, int param) {
    GL11.glLightModelf(pname, param);
  }

  @Override
  public void glLightModelxv(int pname, int[] params, int offset) {
    GL11.glLightModelf(pname, params[offset]);
  }

  @Override
  public void glLightModelxv(int pname, IntBuffer params) {
    GL11.glLightModel(pname, params);
  }

  @Override
  public void glLightf(int light, int pname, float param) {
    GL11.glLightf(light, pname, param);
  }

  @Override
  public void glLightfv(int light, int pname, float[] params, int offset) {
    GL11.glLightf(light, pname, params[offset]);
  }

  @Override
  public void glLightfv(int light, int pname, FloatBuffer params) {
    GL11.glLight(light, pname, params);
  }

  @Override
  public void glLightx(int light, int pname, int param) {
    GL11.glLightf(light, pname, param);
  }

  @Override
  public void glLightxv(int light, int pname, int[] params, int offset) {
    GL11.glLightf(light, pname, params[offset]);
  }

  @Override
  public void glLightxv(int light, int pname, IntBuffer params) {
    GL11.glLight(light, pname, params);
  }

  @Override
  public void glLineWidth(float width) {
    GL11.glLineWidth(width);
  }

  @Override
  public void glLineWidthx(int width) {
    GL11.glLineWidth(width);
  }

  @Override
  public void glLoadIdentity() {
    GL11.glLoadIdentity();
  }

  @Override
  public void glLoadMatrixf(float[] m, int offset) {
    FloatBuffer buffer = (FloatBuffer) BufferUtils.createFloatBuffer(m.length).put(m).position(offset);
    GL11.glLoadMatrix(buffer);
  }

  @Override
  public void glLoadMatrixf(FloatBuffer m) {
    GL11.glLoadMatrix(m);
  }

  @Override
  public void glLoadMatrixx(int[] m, int offset) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(m.length);
    for (int i : m) {
      buffer.put(i);
    }
    buffer.position(offset);
    GL11.glLoadMatrix(buffer);
  }

  @Override
  public void glLoadMatrixx(IntBuffer m) {
    throw new UnsupportedOperationException("glLoadMatrixx");
  }

  @Override
  public void glLogicOp(int opcode) {
    GL11.glLogicOp(opcode);
  }

  @Override
  public void glMaterialf(int face, int pname, float param) {
    GL11.glMaterialf(face, pname, param);
  }

  @Override
  public void glMaterialfv(int face, int pname, float[] params, int offset) {
    GL11.glMaterialf(face, pname, params[offset]);
  }

  @Override
  public void glMaterialfv(int face, int pname, FloatBuffer params) {
    GL11.glMaterial(face, pname, params);
  }

  @Override
  public void glMaterialx(int face, int pname, int param) {
    GL11.glMateriali(face, pname, param);
  }

  @Override
  public void glMaterialxv(int face, int pname, int[] params, int offset) {
    GL11.glMateriali(face, pname, params[offset]);
  }

  @Override
  public void glMaterialxv(int face, int pname, IntBuffer params) {
    GL11.glMaterial(face, pname, params);
  }

  @Override
  public void glMatrixMode(int mode) {
    GL11.glMatrixMode(mode);
  }

  @Override
  public void glMultMatrixf(float[] m, int offset) {
    FloatBuffer buffer = (FloatBuffer) BufferUtils.createFloatBuffer(m.length).put(m).position(offset);
    GL11.glMultMatrix(buffer);
  }

  @Override
  public void glMultMatrixf(FloatBuffer m) {
    GL11.glMultMatrix(m);
  }

  @Override
  public void glMultMatrixx(int[] m, int offset) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(m.length);
    for (int i : m) {
      buffer.put(i);
    }
    buffer.position(offset);
    GL11.glMultMatrix(buffer);
  }

  @Override
  public void glMultMatrixx(IntBuffer m) {
    FloatBuffer buffer = BufferUtils.createFloatBuffer(m.capacity());
    for (int i : m.array()) {
      buffer.put(i);
    }
    GL11.glMultMatrix(buffer);
  }

  @Override
  public void glMultiTexCoord4f(int target, float s, float t, float r, float q) {
    GL13.glMultiTexCoord4f(target, s, t, r, q);
  }

  @Override
  public void glMultiTexCoord4x(int target, int s, int t, int r, int q) {
    GL13.glMultiTexCoord4f(target, s, t, r, q);
  }

  @Override
  public void glNormal3f(float nx, float ny, float nz) {
    GL11.glNormal3f(nx, ny, nz);
  }

  @Override
  public void glNormal3x(int nx, int ny, int nz) {
    GL11.glNormal3f(nx, ny, nz);
  }

  @Override
  public void glNormalPointer(int type, int stride, Buffer pointer) {
    GL11.glNormalPointer(type, stride, (ByteBuffer) pointer);
  }

  @Override
  public void glOrthof(float left, float right, float bottom, float top, float zNear, float zFar) {
    GL11.glOrtho(left, right, bottom, top, zNear, zFar);
  }

  @Override
  public void glOrthox(int left, int right, int bottom, int top, int zNear, int zFar) {
    GL11.glOrtho(left, right, bottom, top, zNear, zFar);
  }

  @Override
  public void glPixelStorei(int pname, int param) {
    GL11.glPixelStorei(pname, param);
  }

  @Override
  public void glPointSize(float size) {
    GL11.glPointSize(size);
  }

  @Override
  public void glPointSizex(int size) {
    GL11.glPointSize(size);
  }

  @Override
  public void glPolygonOffset(float factor, float units) {
    GL11.glPolygonOffset(factor, units);
  }

  @Override
  public void glPolygonOffsetx(int factor, int units) {
    GL11.glPolygonOffset(factor, units);
  }

  @Override
  public void glPopMatrix() {
    GL11.glPopMatrix();
  }

  @Override
  public void glPushMatrix() {
    GL11.glPushMatrix();
  }

  @Override
  public void glReadPixels(int x, int y, int width, int height, int format, int type, Buffer pixels) {
    GL11.glReadPixels(x, y, width, height, format, type, (ByteBuffer) pixels);
  }

  @Override
  public void glRotatef(float angle, float x, float y, float z) {
    GL11.glRotatef(angle, x, y, z);
  }

  @Override
  public void glRotatex(int angle, int x, int y, int z) {
    GL11.glRotatef(angle, x, y, z);
  }

  @Override
  public void glSampleCoverage(float value, boolean invert) {
    GL13.glSampleCoverage(value, invert);
  }

  @Override
  public void glSampleCoveragex(int value, boolean invert) {
    GL13.glSampleCoverage(value, invert);
  }

  @Override
  public void glScalef(float x, float y, float z) {
    GL11.glScalef(x, y, z);
  }

  @Override
  public void glScalex(int x, int y, int z) {
    GL11.glScalef(x, y, z);
  }

  @Override
  public void glScissor(int x, int y, int width, int height) {
    GL11.glScissor(x, y, width, height);
  }

  @Override
  public void glShadeModel(int mode) {
    GL11.glShadeModel(mode);
  }

  @Override
  public void glStencilFunc(int func, int ref, int mask) {
    GL11.glStencilFunc(func, ref, mask);
  }

  @Override
  public void glStencilMask(int mask) {
    GL11.glStencilMask(mask);
  }

  @Override
  public void glStencilOp(int fail, int zfail, int zpass) {
    GL11.glStencilOp(fail, zfail, zpass);
  }

  @Override
  public void glTexCoordPointer(int size, int type, int stride, Buffer pointer) {
    GL11.glTexCoordPointer(size, stride, (FloatBuffer) pointer);
  }

  @Override
  public void glTexEnvf(int target, int pname, float param) {
    GL11.glTexEnvf(target, pname, param);
  }

  @Override
  public void glTexEnvfv(int target, int pname, float[] params, int offset) {
    GL11.glTexEnvf(target, pname, params[offset]);
  }

  @Override
  public void glTexEnvfv(int target, int pname, FloatBuffer params) {
    GL11.glTexEnv(target, pname, params);
  }

  @Override
  public void glTexEnvx(int target, int pname, int param) {
    GL11.glTexEnvi(target, pname, param);
  }

  @Override
  public void glTexEnvxv(int target, int pname, int[] params, int offset) {
    GL11.glTexEnvf(target, pname, params[offset]);
  }

  @Override
  public void glTexEnvxv(int target, int pname, IntBuffer params) {
    GL11.glTexEnv(target, pname, params);
  }

  @Override
  public void glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels) {
    GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, (IntBuffer) pixels);
    /*  glTexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {*/
  }

  @Override
  public void glTexParameterf(int target, int pname, float param) {
    GL11.glTexParameterf(target, pname, param);
  }

  @Override
  public void glTexParameterx(int target, int pname, int param) {
    GL11.glTexParameteri(target, pname, param);
  }

  @Override
  public void glTexSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, Buffer pixels) {
    GL11.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, (ByteBuffer) pixels);
  }

  @Override
  public void glTranslatef(float x, float y, float z) {
    GL11.glTranslatef(x, y, z);
  }

  @Override
  public void glTranslatex(int x, int y, int z) {
    GL11.glTranslatef(x, y, z);
  }

  @Override
  public void glVertexPointer(int size, int type, int stride, Buffer pointer) {
    GL11.glVertexPointer(size, stride, (FloatBuffer) pointer);
  }

  @Override
  public void glViewport(int x, int y, int width, int height) {
    GL11.glViewport(x, y, width, height);
  }

  @Override
  public void glBindBuffer(int target, int buffer) {
    GL15.glBindBuffer(target, buffer);
  }

  @Override
  public void glBufferData(int target, int size, Buffer data, int usage) {
    GL15.glBufferData(target, (ByteBuffer) data, usage);
  }

  @Override
  public void glBufferSubData(int target, int offset, int size, Buffer data) {
    GL15.glBufferSubData(target, offset, (ByteBuffer) data);
  }

  @Override
  public void glClipPlanef(int plane, float[] equation, int offset) {
    DoubleBuffer buffer = BufferUtils.createDoubleBuffer(equation.length);
    for (float f : equation) {
      buffer.put(f);
    }
    GL11.glClipPlane(plane, buffer);
  }

  @Override
  public void glClipPlanef(int plane, FloatBuffer equation) {
    DoubleBuffer buffer = BufferUtils.createDoubleBuffer(equation.capacity());
    for (float f : equation.array()) {
      buffer.put(f);
    }
    GL11.glClipPlane(plane, buffer);
  }

  @Override
  public void glClipPlanex(int plane, int[] equation, int offset) {
    DoubleBuffer buffer = BufferUtils.createDoubleBuffer(1).put(equation[offset]);
    GL11.glClipPlane(plane, buffer);
  }

  @Override
  public void glClipPlanex(int plane, IntBuffer equation) {
    DoubleBuffer buffer = BufferUtils.createDoubleBuffer(equation.capacity());
    for (float f : equation.array()) {
      buffer.put(f);
    }
    GL11.glClipPlane(plane, buffer);
  }

  @Override
  public void glColor4ub(byte red, byte green, byte blue, byte alpha) {
    GL11.glColor4ub(red, green, blue, alpha);
  }

  @Override
  public void glColorPointer(int size, int type, int stride, int offset) {
    GL11.glColorPointer(size, type, stride, offset);
  }

  @Override
  public void glDeleteBuffers(int n, int[] buffers, int offset) {
    GL15.glDeleteBuffers(buffers[offset]);
  }

  @Override
  public void glDeleteBuffers(int n, IntBuffer buffers) {
    GL15.glDeleteBuffers(buffers);
  }

  @Override
  public void glDrawElements(int mode, int count, int type, int offset) {
    GL11.glDrawElements(mode, count, type, offset);
  }

  @Override
  public void glGenBuffers(int n, int[] buffers, int offset) {
    IntBuffer buffer = (IntBuffer) BufferUtils.createIntBuffer(buffers.length).put(buffers).position(offset);
    GL15.glGenBuffers(buffer);
  }

  @Override
  public void glGenBuffers(int n, IntBuffer buffers) {
    GL15.glGenBuffers(buffers);
  }

  @Override
  public void glGetBooleanv(int pname, boolean[] params, int offset) {
    ByteBuffer buffer = BufferUtils.createByteBuffer(params.length);
    for (boolean b : params) {
      buffer.put((byte) (b ? 1 : 0));
    }
    buffer.position(offset);
    GL11.glGetBoolean(pname, buffer);
  }

  @Override
  public void glGetBooleanv(int pname, IntBuffer params) {
    ByteBuffer buffer = BufferUtils.createByteBuffer(params.capacity());
    for (int b : params.array()) {
      buffer.put((byte) b);
    }
    GL11.glGetBoolean(pname, buffer);
  }

  @Override
  public void glGetBufferParameteriv(int target, int pname, int[] params, int offset) {
    IntBuffer buffer = (IntBuffer) BufferUtils.createIntBuffer(params.length).put(params).position(offset);
    GL15.glGetBufferParameter(target, pname, buffer);
  }

  @Override
  public void glGetBufferParameteriv(int target, int pname, IntBuffer params) {
    GL15.glGetBufferParameter(target, pname, params);
  }

  @Override
  public void glGetClipPlanef(int pname, float[] eqn, int offset) {
    DoubleBuffer buffer = BufferUtils.createDoubleBuffer(eqn.length);
    for (float f : eqn) {
      buffer.put(f);
    }
    GL11.glGetClipPlane(pname, buffer);
  }

  @Override
  public void glGetClipPlanef(int pname, FloatBuffer eqn) {
    DoubleBuffer buffer = BufferUtils.createDoubleBuffer(eqn.capacity());
    for (float f : eqn.array()) {
      buffer.put(f);
    }
    GL11.glGetClipPlane(pname, buffer);
  }

  @Override
  public void glGetClipPlanex(int pname, int[] eqn, int offset) {
    DoubleBuffer buffer = BufferUtils.createDoubleBuffer(eqn.length);
    for (int f : eqn) {
      buffer.put(f);
    }
    GL11.glGetClipPlane(pname, buffer);
  }

  @Override
  public void glGetClipPlanex(int pname, IntBuffer eqn) {
    DoubleBuffer buffer = BufferUtils.createDoubleBuffer(eqn.capacity());
    for (int f : eqn.array()) {
      buffer.put(f);
    }
    GL11.glGetClipPlane(pname, buffer);
  }

  @Override
  public void glGetFixedv(int pname, int[] params, int offset) {
    throw new UnsupportedOperationException("glGetFixedv");
  }

  @Override
  public void glGetFixedv(int pname, IntBuffer params) {
    throw new UnsupportedOperationException("glGetFixedv");
  }

  @Override
  public void glGetFloatv(int pname, float[] params, int offset) {
    FloatBuffer buffer = (FloatBuffer) BufferUtils.createFloatBuffer(params.length).put(params).position(offset);
    GL11.glGetFloat(pname, buffer);
  }

  @Override
  public void glGetFloatv(int pname, FloatBuffer params) {
    GL11.glGetFloat(pname, params);
  }

  @Override
  public void glGetLightfv(int light, int pname, float[] params, int offset) {
    FloatBuffer buffer = (FloatBuffer) BufferUtils.createFloatBuffer(params.length).put(params).position(offset);
    GL11.glGetLight(light, pname, buffer);
  }

  @Override
  public void glGetLightfv(int light, int pname, FloatBuffer params) {
    GL11.glGetLight(light, pname, params);
  }

  @Override
  public void glGetLightxv(int light, int pname, int[] params, int offset) {
    IntBuffer buffer = (IntBuffer) BufferUtils.createIntBuffer(params.length).put(params).position(offset);
    GL11.glGetLight(light, pname, buffer);
  }

  @Override
  public void glGetLightxv(int light, int pname, IntBuffer params) {
    GL11.glGetLight(light, pname, params);
  }

  @Override
  public void glGetMaterialfv(int face, int pname, float[] params, int offset) {
    FloatBuffer buffer = (FloatBuffer) BufferUtils.createFloatBuffer(params.length).put(params).position(offset);
    GL11.glGetMaterial(face, pname, buffer);
  }

  @Override
  public void glGetMaterialfv(int face, int pname, FloatBuffer params) {
    GL11.glGetMaterial(face, pname, params);
  }

  @Override
  public void glGetMaterialxv(int face, int pname, int[] params, int offset) {
    IntBuffer buffer = (IntBuffer) BufferUtils.createIntBuffer(params.length).put(params).position(offset);
    GL11.glGetMaterial(face, pname, buffer);
  }

  @Override
  public void glGetMaterialxv(int face, int pname, IntBuffer params) {
    GL11.glGetMaterial(face, pname, params);
  }

  @Override
  public void glGetTexEnvfv(int env, int pname, float[] params, int offset) {
    FloatBuffer buffer = (FloatBuffer) BufferUtils.createFloatBuffer(params.length).put(params).position(offset);
    GL11.glGetTexEnv(env, pname, buffer);
  }

  @Override
  public void glGetTexEnvfv(int env, int pname, FloatBuffer params) {
    GL11.glGetTexEnv(env, pname, params);
  }

  @Override
  public void glGetTexEnviv(int env, int pname, int[] params, int offset) {
    IntBuffer buffer = (IntBuffer) BufferUtils.createIntBuffer(params.length).put(params).position(offset);
    GL11.glGetTexEnv(env, pname, buffer);
  }

  @Override
  public void glGetTexEnviv(int env, int pname, IntBuffer params) {
    GL11.glGetTexEnv(env, pname, params);
  }

  @Override
  public void glGetTexEnvxv(int env, int pname, int[] params, int offset) {
    IntBuffer buffer = (IntBuffer) BufferUtils.createIntBuffer(params.length).put(params).position(offset);
    GL11.glGetTexEnv(env, pname, buffer);
  }

  @Override
  public void glGetTexEnvxv(int env, int pname, IntBuffer params) {
    GL11.glGetTexEnv(env, pname, params);
  }

  @Override
  public void glGetTexParameterfv(int target, int pname, float[] params, int offset) {
    FloatBuffer buffer = (FloatBuffer) BufferUtils.createFloatBuffer(params.length).put(params).position(offset);
    GL11.glGetTexParameter(target, pname, buffer);
  }

  @Override
  public void glGetTexParameterfv(int target, int pname, FloatBuffer params) {
    GL11.glGetTexParameter(target, pname, params);
  }

  @Override
  public void glGetTexParameteriv(int target, int pname, int[] params, int offset) {
    IntBuffer buffer = (IntBuffer) BufferUtils.createIntBuffer(params.length).put(params).position(offset);
    GL11.glGetTexParameter(target, pname, buffer);
  }

  @Override
  public void glGetTexParameteriv(int target, int pname, IntBuffer params) {
    GL11.glGetTexParameter(target, pname, params);
  }

  @Override
  public void glGetTexParameterxv(int target, int pname, int[] params, int offset) {
    IntBuffer buffer = (IntBuffer) BufferUtils.createIntBuffer(params.length).put(params).position(offset);
    GL11.glGetTexParameter(target, pname, buffer);
  }

  @Override
  public void glGetTexParameterxv(int target, int pname, IntBuffer params) {
    GL11.glGetTexParameter(target, pname, params);
  }

  @Override
  public boolean glIsBuffer(int buffer) {
    return GL15.glIsBuffer(buffer);
  }

  @Override
  public boolean glIsEnabled(int cap) {
    return GL11.glIsEnabled(cap);
  }

  @Override
  public boolean glIsTexture(int texture) {
    return GL11.glIsTexture(texture);
  }

  @Override
  public void glNormalPointer(int type, int stride, int offset) {
    GL11.glNormalPointer(type, stride, offset);
  }

  @Override
  public void glPointParameterf(int pname, float param) {
    GL14.glPointParameterf(pname, param);
  }

  @Override
  public void glPointParameterfv(int pname, float[] params, int offset) {
    GL14.glPointParameterf(pname, params[offset]);
  }

  @Override
  public void glPointParameterfv(int pname, FloatBuffer params) {
    GL14.glPointParameter(pname, params);
  }

  @Override
  public void glPointParameterx(int pname, int param) {
    GL14.glPointParameteri(pname, param);
  }

  @Override
  public void glPointParameterxv(int pname, int[] params, int offset) {
    GL14.glPointParameteri(pname, params[offset]);
  }

  @Override
  public void glPointParameterxv(int pname, IntBuffer params) {
    GL14.glPointParameter(pname, params);
  }

  @Override
  public void glPointSizePointerOES(int type, int stride, Buffer pointer) {
    throw new UnsupportedOperationException("glPointSizePointerOES");
  }

  @Override
  public void glTexCoordPointer(int size, int type, int stride, int offset) {
    GL11.glTexCoordPointer(size, type, stride, offset);
  }

  @Override
  public void glTexEnvi(int target, int pname, int param) {
    GL11.glTexEnvi(target, pname, param);
  }

  @Override
  public void glTexEnviv(int target, int pname, int[] params, int offset) {
    GL11.glTexEnvi(target, pname, params[offset]);
  }

  @Override
  public void glTexEnviv(int target, int pname, IntBuffer params) {
    GL11.glTexEnv(target, pname, params);
  }

  @Override
  public void glTexParameterfv(int target, int pname, float[] params, int offset) {
    GL11.glTexParameterf(target, pname, params[offset]);
  }

  @Override
  public void glTexParameterfv(int target, int pname, FloatBuffer params) {
    GL11.glTexParameter(target, pname, params);
  }

  @Override
  public void glTexParameteri(int target, int pname, int param) {
    GL11.glTexParameteri(target, pname, param);
  }

  @Override
  public void glTexParameteriv(int target, int pname, int[] params, int offset) {
    GL11.glTexParameteri(target, pname, params[offset]);
  }

  @Override
  public void glTexParameteriv(int target, int pname, IntBuffer params) {
    GL11.glTexParameter(target, pname, params);
  }

  @Override
  public void glTexParameterxv(int target, int pname, int[] params, int offset) {
    GL11.glTexParameteri(target, pname, params[offset]);
  }

  @Override
  public void glTexParameterxv(int target, int pname, IntBuffer params) {
    GL11.glTexParameter(target, pname, params);
  }

  @Override
  public void glVertexPointer(int size, int type, int stride, int offset) {
    GL11.glVertexPointer(size, type, stride, offset);
  }
}
