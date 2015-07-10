package com.mycompany.traveljournal.openGL;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Created by sjayaram on 7/9/2015.
 */
public class MyGLSurfaceView extends GLSurfaceView {
    MyGLRenderer renderer; // Custom GL Renderer

    // For touch event
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320.0f;
    private float previousX;
    private float previousY;

    // Constructor - Allocate and set the renderer
    public MyGLSurfaceView(Context context, String postId) {
        super(context);
        renderer = new MyGLRenderer(context, postId);
        this.setRenderer(renderer);
        // Request focus, otherwise key/button won't react
        this.requestFocus();
        this.setFocusableInTouchMode(true);
    }

    @Override
    public boolean callOnClick() {
        renderer.blendingEnabled = !renderer.blendingEnabled;
        return super.callOnClick();
    }

    @Override
    public boolean hasOnClickListeners() {
        renderer.lightingEnabled = !renderer.lightingEnabled;
        return super.hasOnClickListeners();
    }

    // Handler for key event
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent evt) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_LEFT: // Decrease Y-rotational speed
                renderer.speedY -= 0.1f;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT: // Increase Y-rotational speed
                renderer.speedY += 0.1f;
                break;
            case KeyEvent.KEYCODE_DPAD_UP: // Decrease X-rotational speed
                renderer.speedX -= 0.1f;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN: // Increase X-rotational speed
                renderer.speedX += 0.1f;
                break;
            case KeyEvent.KEYCODE_A: // Zoom out (decrease z)
                renderer.z -= 0.2f;
                break;
            case KeyEvent.KEYCODE_Z: // Zoom in (increase z)
                renderer.z += 0.2f;
                break;
            case KeyEvent.KEYCODE_F:  //KEYCODE_DPAD_CENTER:  // Select texture filter (NEW)
                renderer.currentTextureFilter = (renderer.currentTextureFilter + 1) % 3;
                break;

            case KeyEvent.KEYCODE_L:  // Toggle lighting on/off (NEW)
                renderer.lightingEnabled = !renderer.lightingEnabled;
                break;
            case KeyEvent.KEYCODE_B:  // Toggle Blending on/off (NEW)
                renderer.blendingEnabled = !renderer.blendingEnabled;
                break;

        }
        return true; // Event handled
    }

    // Handler for touch event
    @Override
    public boolean onTouchEvent(final MotionEvent evt) {
        float currentX = evt.getX();
        float currentY = evt.getY();
        float deltaX, deltaY;
        switch (evt.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // Modify rotational angles according to movement
                deltaX = currentX - previousX;
                deltaY = currentY - previousY;
                renderer.angleX += deltaY * TOUCH_SCALE_FACTOR;
                renderer.angleY += deltaX * TOUCH_SCALE_FACTOR;
        }
        // Save current x, y
        previousX = currentX;
        previousY = currentY;
        return true; // Event handled
    }
}
