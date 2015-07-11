package com.mycompany.traveljournal.openGL;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by sjayaram on 7/9/2015.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {
    Context context;
    private PhotoCube cube;

    // For controlling cube's z-position, x and y angles and speeds (NEW)
    float angleX = 0;   // (NEW)
    float angleY = 0;   // (NEW)
    float speedX = 0;   // (NEW)
    float speedY = 0;   // (NEW)
    float z = -6.0f;    // (NEW)

    int currentTextureFilter = 0;  // Texture filter

    // Lighting (NEW)
    boolean lightingEnabled = false;   // Is lighting on? (NEW)
    // Blending (NEW)
    boolean blendingEnabled = false;  // Is blending on? (NEW)

    private float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
    private float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] lightPosition = {0.0f, 0.0f, 2.0f, 1.0f};

    public MyGLRenderer(Context context, String postId) {
        this.context = context;
        cube = new PhotoCube();
        cube.setImages(postId);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);  // Set color's clear-value to
        // black
        gl.glClearDepthf(1.0f); // Set depth's clear-value to farthest
        gl.glEnable(GL10.GL_DEPTH_TEST); // Enables depth-buffer for hidden
        // surface removal
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST); // nice
        // perspective
        // view
        gl.glShadeModel(GL10.GL_SMOOTH); // Enable smooth shading of color
        gl.glDisable(GL10.GL_DITHER); // Disable dithering for better
        // performance

        cube.loadGLTexture(gl, context);

        gl.glEnable(GL10.GL_TEXTURE_2D);  // Enable texture (NEW)

        // Setup lighting GL_LIGHT1 with ambient and diffuse lights (NEW)
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, lightAmbient, 0);
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, lightDiffuse, 0);
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, lightPosition, 0);
        gl.glEnable(GL10.GL_LIGHT1);   // Enable Light 1 (NEW)
        gl.glEnable(GL10.GL_LIGHT0);   // Enable the default Light 0 (NEW)

        // Setup Blending (NEW)
        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);           // Full brightness, 50% alpha (NEW)
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE); // Select blending function (NEW)

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // Clear color and depth buffers using clear-value set earlier
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // You OpenGL|ES rendering code here
        // Enable lighting? (NEW)
        if (lightingEnabled) {
            gl.glEnable(GL10.GL_LIGHTING);
        } else {
            gl.glDisable(GL10.GL_LIGHTING);
        }

        // Blending Enabled? (NEW)
        if (blendingEnabled) {
            gl.glEnable(GL10.GL_BLEND);       // Turn blending on (NEW)
            gl.glDisable(GL10.GL_DEPTH_TEST); // Turn depth testing off (NEW)

        } else {
            gl.glDisable(GL10.GL_BLEND);      // Turn blending off (NEW)
            gl.glEnable(GL10.GL_DEPTH_TEST);  // Turn depth testing on (NEW)
        }

        // ----- Render the Cube -----
        gl.glLoadIdentity();              // Reset the model-view matrix
        gl.glTranslatef(0.0f, 0.0f, z);   // Translate into the screen (NEW)
        gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f); // Rotate (NEW)
        gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f); // Rotate (NEW)

        cube.draw(gl);
        //cube.draw(gl, currentTextureFilter);

        // Update the rotational angle after each refresh (NEW)
        angleX += speedX;  // (NEW)
        angleY += speedY;  // (NEW)

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (height == 0)
            height = 1;
        float aspect = (float) width / height;
        // Set the viewport (display area) to cover the entire window
        // set the Android's view port (display area) to cover the entire screen
        // from (0,0) to (width-1, height-1):
        gl.glViewport(0, 0, width, height);
        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity(); // reset projection matrix
        // Use perspective projection
        GLU.gluPerspective(gl, 45, aspect, 0.1f, 100.f);

        gl.glMatrixMode(GL10.GL_MODELVIEW); // Select model-view matrix
        gl.glLoadIdentity(); // reset

    }

}
