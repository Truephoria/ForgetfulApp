package com.example.trevor.forgetfultransfer;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;

import com.google.android.gms.fitness.SensorsApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.security.acl.Group;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;

import javax.microedition.khronos.opengles.GL10;


public class OpenGLESRenderer extends AppCompatActivity implements SensorEventListener {



    private FusedLocationProviderClient intentService;

//    private HandlerThread mSensorThread;
//    private Handler mSensorHandler;

    public float orientationX;
    public float orientationY;
    public float orientationZ;

    Sensor OrientationSensor;
    Sensor accelerometer;
    Sensor magnetometer;

    public float[] mGravity;
    public float[] mGeomagnetic;
    public float azimuth;
    public float roundedHeading;
    public float transformAngle;
    public float angleB;
    public float directionalBearing;
    public float relativeTransform;
    public SensorManager mSensorManager;



//    SensorManager mSensorManager;


    public GLSurfaceView mGLView;

    ListDataActivity mListActivity;
    EditDataActivity mEditDataActivity;

    MyGLRenderer mMyGLRenderer;
    private MyGLRenderer mRenderer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_glesrenderer);
        mGLView = new MyGLSurfaceView(this);
        mListActivity = new ListDataActivity();
        mEditDataActivity = new EditDataActivity();

        mSensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        OrientationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mSensorManager.registerListener(OpenGLESRenderer.this, OrientationSensor, mSensorManager.SENSOR_DELAY_NORMAL);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(OpenGLESRenderer.this, accelerometer, mSensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(OpenGLESRenderer.this, magnetometer, mSensorManager.SENSOR_DELAY_NORMAL);


//        mMyGLRenderer = new MyGLRenderer();
        setContentView(mGLView);
//        run();


    }


    @Override
    public ComponentName startService(Intent service) {
        return super.startService(service);
    }

    public static class MyGLRenderer implements GLSurfaceView.Renderer, SensorEventListener {

//        OpenGLESRenderer mOpenGLESRenderer;


        MyGLSurfaceView mMyGLSurfaceView;

        public volatile float mAngle;
        public volatile float mbAngle;



        public float getAngle() {
            return mAngle;
        }


        public void setAngle(float angle) {
            mAngle = angle;
        }

        public float getbAngle() {
            return mbAngle;
        }


        public void setbAngle(float angle) {
            mbAngle = angle;
        }


        private final float[] mMVPMatrix = new float[16];
        private final float[] mProjectionMatrix = new float[16];
        private final float[] mViewMatrix = new float[16];
        private float[] mRotationMatrix = new float[16];


        //        public static float orX;
        public float orY;
        public float orZ;
        public float orX;


        private Triangle mTriangle;
        private Square mSquare;

        public void onSurfaceCreated(GLES20 unused, EGLConfig config) {
            // Set the background frame color
            GLES20.glClearColor(255.0f, 0.0f, 0.0f, 1.0f);
        }

        public void onDrawFrame(GL10 unused) {



//            mOpenGLESRenderer = new OpenGLESRenderer();


            float[] scratch = new float[16];


//            setAngle(
//                   getAngle() +
//                            (orX));


//            long time = SystemClock.uptimeMillis() % 4000L;
//            float angle = 0.090f * ((int) time);
//            Matrix.rotateM(mRotationMatrix, 0, mbAngle, 0, 0, -1.0f);
//            glPushMatrix();

            Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);

//            Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);


            Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

            // Redraw background color
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            // Set the camera position (View matrix)
            Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

            // Calculate the projection and view transformation
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);


            // Draw shape
            mTriangle.draw(scratch);

//            glPopMatrix();


//            glPushMatrix();
//
//            Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);
//
////            Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
//
//            // Redraw background color
////            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
//
//            // Set the camera position (View matrix)
//            Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//
//            // Calculate the projection and view transformation
//            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
//
//
//            glPopMatrix();


        }

        @Override
        public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
            GLES20.glClearColor(255.0f, 0.0f, 0.0f, 1.0f);


            mTriangle = new Triangle();
            mSquare = new Square();


        }

        public void onSurfaceChanged(GL10 unused, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
            float ratio = (float) width / height;

            Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        }


        public static int loadShader(int type, String shaderCode) {

            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            int shader = GLES20.glCreateShader(type);

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);

            return shader;
        }


        @Override
        public void onSensorChanged(SensorEvent event) {

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }


    public class MyGLSurfaceView extends GLSurfaceView implements SensorEventListener {



        private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
        private float mPreviousX;
        private float mPreviousY;
        public float orientX;
        public float orientY;
        public float orientZ;



        private SensorManager mSensorManager;

        public final MyGLRenderer mRenderer;
        private FusedLocationProviderClient locRenderClient;



        public MyGLSurfaceView(Context context) {
            super(context);
            locRenderClient = LocationServices.getFusedLocationProviderClient(OpenGLESRenderer.this);


            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);



            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            mRenderer = new MyGLRenderer();


            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(mRenderer);


            Intent serviceIntent = new Intent(context, MyGLSurfaceView.class);
            context.startService(serviceIntent);

            Intent sendBearing = getIntent();
            String bearingDirection = sendBearing.getStringExtra("set Bearing");
            String northDeclination = sendBearing.getStringExtra("set Declination");
            String chosenLatitude = sendBearing.getStringExtra("LatDestination");
            String chosenLongitude = sendBearing.getStringExtra("LongDestination");

//            final float latitudeDestination = Float.parseFloat(chosenLatitude);
//            final float longitudeDestination = Float.parseFloat(chosenLongitude);

            final Double DLatitudeDestination = Double.parseDouble(chosenLatitude);
            final Double DLongitudeDestination = Double.parseDouble(chosenLongitude);

            float bearingToDestination = Float.parseFloat(bearingDirection);
            String orientXString = Float.toString(orientX);

            float northernDeclination = Float.parseFloat(northDeclination);
            String NDeclinationString = Float.toString(northernDeclination);

            Log.d("Render NDeclination = ", NDeclinationString);

            Log.d("Render Bearing =", bearingDirection);
            Log.d("OrientX =", orientXString);

            if (ActivityCompat.checkSelfPermission(OpenGLESRenderer.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            locRenderClient.getLastLocation().addOnSuccessListener(OpenGLESRenderer.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Location locInfo = location;

                        Location targetDestination = TargetDestination(DLatitudeDestination,DLongitudeDestination);


                        locInfo.bearingTo(targetDestination);
                        float locDirection = locInfo.bearingTo(targetDestination);
                        String locDirectionString = Float.toString(locDirection);
                        Log.d("LOCDIRECTIONSTRING =", locDirectionString);

                        GeomagneticField geomagneticField = new GeomagneticField((float)location.getLatitude(),
                                (float)location.getLongitude(), (float)location.getAltitude(),location.getTime());

                       Float geofieldDeclination = geomagneticField.getDeclination();


//                        Double geofieldDouble = Double.parseDouble(geofieldString);
//                        Integer geofieldINT = Integer.parseInt(geofieldString);

                        Float relativeDirection = locDirection - geofieldDeclination;


//                        mRenderer.setAngle(
//                                mRenderer.getAngle() +
//                                        (relativeDirection + orientationX) );


                    }

                }
            });


//            locRenderClient.getLastLocation();


//                orientX - bearingToDestination




//           transformAngle = getBearingRotationAngle(roundedHeading, bearingToDestination);
//            angleB = transformAngle;





//            String angleBString = Float.toString(angleB);

//            Log.d("ANGLE B", angleBString);


//        if(mEditDataActivity.IDLatitude != null) {


//                getBearingRotationAngle();
//            }


        }

        public void setBearingTo(String destination) {


        }


        @Override
        public boolean onTouchEvent(MotionEvent e) {
            // MotionEvent reports input details from the touch screen
            // and other input controls. In this case, you are only
            // interested in events where the touch position changed.

            float x = e.getX();
            float y = e.getY();

            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE:

                    float dx = x - mPreviousX;
                    float dy = y - mPreviousY;

                    // reverse direction of rotation above the mid-line
                    if (y > getHeight() / 2) {
                        dx = dx * -1;
                    }

                    // reverse direction of rotation to left of the mid-line
                    if (x < getWidth() / 2) {
                        dy = dy * -1;
                    }

//                    mRenderer.setAngle(
//                            mRenderer.getAngle() +
//                                    ((dx + dy) * TOUCH_SCALE_FACTOR));
//                    requestRender();
            }


            mPreviousX = x;
            mPreviousY = y;
            return true;


        }


        @Override
        public void onSensorChanged(SensorEvent event) {

//            String PIString = Double.toString(Math.PI);
//
//            float PIFloat = Float.parseFloat(PIString);
//
//            float angleA = orientX;
//
//
//            String angleAString = Float.toString(angleA);
//
//            Log.d("ANGLE A", angleAString);
//
////            mRenderer.setAngle(
////                    mRenderer.getAngle() +
////                            (event.values[0] *180));
           float orientXscreen = event.values[0];
           String orientXString = Float.toString(orientXscreen);
            Log.d("OrientX = ", orientXString);
//            orientY = event.values[1];
//            orientZ = event.values[2];
//
//            String valueString = Float.toString(event.values[0]);
//            Log.d("Event Value", valueString);
//
            mRenderer.setAngle(
                    mRenderer.getAngle() +
                            ((orientXscreen))
            );




        }





        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }





    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("Renderer Sensor Event:", "X:" + event.values[0] + "  Y:" + event.values[1] + "  Z:" + event.values[2]);

       orientationX = event.values[0];
       orientationY = event.values[1];
       orientationZ = event.values[2];



//                mMyGLRenderer.setbAngle();

//         );
//        float orientationXX = event.values[0];
//                mMyGLRenderer.setAngle(
//                            mRenderer.getAngle() +
//                                    (orientationXX));



            }











//       mMyGLRenderer.setAngle(
//               mMyGLRenderer.getAngle() +
//                       (X) + 50);


//        getBearingRotationAngle(X,Y,Z);


    public float getBearingRotationAngle(float a, float b) {




        Log.d("Renderer", "get that rotation angle down homie!");


 float relativeAngle = b - a;

 return relativeAngle;

//        String XString = Float.toString(orientationX);
//
//        Log.d("renderer float X =", XString);





//            cameraRotAngle = x;
//             ArrowRotAngle = location.bearingTo(targetDest);

    }

//    public void run() {
//
//        if (Looper.myLooper()==null) {
//        locRenderClient = LocationServices.getFusedLocationProviderClient(this);
//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        Sensor handlerSensor = this.mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
//         Looper.getMainLooper();
//        Looper.prepareMainLooper();
//
//        Handler handler = new Handler();
//        Log.d("LOOPER THREAD", "Looper thread running");
//
//        mSensorManager.registerListener(OpenGLESRenderer.this, handlerSensor, SensorManager.SENSOR_DELAY_UI, handler);
//        Looper.loop();
//        }
//    }
public Location TargetDestination (Double selectedLat, Double selectedLong) {

    Location targetDestination = new Location("");

    if(selectedLat != null) {
        targetDestination.setLatitude(selectedLat);
        targetDestination.setLongitude(selectedLong);
    }

    return targetDestination;

}


}





