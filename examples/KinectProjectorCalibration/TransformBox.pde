

class TransformBox {
  int id;
  PMatrix3D mat;
  TransformBox() {
    mat = new PMatrix3D();
  }

  void setTranslation(float x, float y, float z) {
    mat.translate(x, y, z);
  }

  void setRotation(float x, float y, float z) {
    mat.rotateX(radians(x));
    mat.rotateY(radians(y));
    mat.rotateZ(radians(z));
  }
  
  void setScale(float s){
    mat.scale(s);
  }


  float[] getTranslation() {
    return new float[]{ mat.m03, mat.m13, mat.m23 };
  }

  float[] getRotation() {
    float ry, rz, rx;
    if (mat.m10 > 0.998) { // singularity at north pole
      ry = atan2(mat.m02, mat.m22);
      rz = HALF_PI;
      rx = 0;
      return new float[]{rx, ry, rz};
    }
    if (mat.m10 < -0.998) { // singularity at south pole
      ry = atan2(mat.m02, mat.m22);
      rz = -HALF_PI;
      rx = 0;
      return new float[]{rx, ry, rz};
    }
    ry = atan2(-mat.m20, mat.m00);
    rx = atan2(-mat.m12, mat.m11);
    rz = asin(mat.m10);
    return new float[]{rx, ry, rz};
  }
}