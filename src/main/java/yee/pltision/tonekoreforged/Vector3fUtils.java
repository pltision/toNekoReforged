package yee.pltision.tonekoreforged;

import org.joml.Matrix3f;

public class Vector3fUtils {
    public static final Matrix3f ROT_X=new Matrix3f().rotateX((float) (Math.PI/2));
    public static final Matrix3f ROT_Y=new Matrix3f().rotateY((float) (Math.PI/2));
    public static final Matrix3f ROT_Z=new Matrix3f().rotateZ((float) (Math.PI/2));
    public static final Matrix3f ROT_NX=new Matrix3f().rotateX((float) (-Math.PI/2));
    public static final Matrix3f ROT_NY=new Matrix3f().rotateY((float) (-Math.PI/2));
    public static final Matrix3f ROT_NZ=new Matrix3f().rotateZ((float) (-Math.PI/2));

}
