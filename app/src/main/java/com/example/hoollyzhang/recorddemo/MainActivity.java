package com.example.hoollyzhang.recorddemo;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import org.wysaid.camera.CameraInstance;
import org.wysaid.myUtils.FileUtil;
import org.wysaid.myUtils.ImageUtil;
import org.wysaid.view.CameraRecordGLSurfaceView;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {


    private static final int ASK_RECORD_PERMISSION = 202;
    private static final String TAG = "MainActivity";
    TextView bt_start, bt_stop;
    CameraRecordGLSurfaceView mRecordGLSurfaceView;
    public static String lastVideoPathFileName;

    String recordFilename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lastVideoPathFileName = FileUtil.getPath(getApplicationContext()) + "/lastVideoPath.txt";

        mRecordGLSurfaceView = (CameraRecordGLSurfaceView) findViewById(R.id.record_view);

        bt_start = (TextView) findViewById(R.id.start_record);
        bt_stop = (TextView) findViewById(R.id.stop_record);
        findViewById(R.id.change_camera).setOnClickListener(this);

        bt_start.setOnClickListener(this);
        bt_stop.setOnClickListener(this);
        methodRequiresTwoPermission();
    }

    @AfterPermissionGranted(ASK_RECORD_PERMISSION)
    private void methodRequiresTwoPermission() {
        String[] perms = {android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.RECORD_AUDIO};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
            initRecordView();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "请求权限",
                    ASK_RECORD_PERMISSION, perms);
        }
    }

    private void initRecordView() {
        mRecordGLSurfaceView.presetCameraForward(false);
        mRecordGLSurfaceView.setMaskBitmap(null, false);
        mRecordGLSurfaceView.presetRecordingSize(480, 640);
        mRecordGLSurfaceView.setZOrderOnTop(false);
        mRecordGLSurfaceView.setZOrderMediaOverlay(true);
        mRecordGLSurfaceView.setOnCreateCallback(new CameraRecordGLSurfaceView.OnCreateCallback() {
            @Override
            public void createOver(boolean success) {
                if (success) {
                    Log.i(TAG, "view 创建成功");
                    mRecordGLSurfaceView.setFilterWithConfig("@beautify face 1 480 640");
                    mRecordGLSurfaceView.setFilterIntensity(85 / 100f);
                } else {
                    Log.e(TAG, "view 创建失败!");
                }
            }
        });
        mRecordGLSurfaceView.setFitFullView(true);
        mRecordGLSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN: {
                        Log.i(TAG, String.format("Tap to focus: %g, %g", event.getX(), event.getY()));
                        final float focusX = event.getX() / mRecordGLSurfaceView.getWidth();
                        final float focusY = event.getY() / mRecordGLSurfaceView.getHeight();
                        mRecordGLSurfaceView.focusAtPoint(focusX, focusY, new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, Camera camera) {
                                if (success) {
                                    Log.e(TAG, String.format("Focus OK, pos: %g, %g", focusX, focusY));
                                } else {
                                    Log.e(TAG, String.format("Focus failed, pos: %g, %g", focusX, focusY));
                                    mRecordGLSurfaceView.cameraInstance().setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                                }
                            }
                        });
                    }
                    break;
                    default:
                        break;
                }
                return true;
            }
        });
        mRecordGLSurfaceView.setPictureSize(600, 800, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecordGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CameraInstance.getInstance().stopCamera();
        mRecordGLSurfaceView.release(null);
        mRecordGLSurfaceView.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_record:
                Log.i(TAG, "Start recording...");
                mRecordGLSurfaceView.setClearColor(1.0f, 0.0f, 0.0f, 0.3f);
                recordFilename = ImageUtil.getPath(getApplicationContext()) + "/rec_" + System.currentTimeMillis() + ".mp4";
                mRecordGLSurfaceView.startRecording(recordFilename, new CameraRecordGLSurfaceView.StartRecordingCallback() {
                    @Override
                    public void startRecordingOver(boolean success) {
                        if (success) {
                            Log.i(TAG, "启动录制成功g...");
                            FileUtil.saveTextContent(recordFilename, lastVideoPathFileName);
                        } else {
                            Log.i(TAG, "启动录制失败...");
                        }
                    }
                });
                break;
            case R.id.stop_record:
                Log.i(TAG, "录制完毕， 存储为 " + recordFilename);
                Log.i(TAG, "End recording...");
                mRecordGLSurfaceView.setClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                mRecordGLSurfaceView.endRecording(new CameraRecordGLSurfaceView.EndRecordingCallback() {
                    @Override
                    public void endRecordingOK() {
                        Log.i(TAG, "End recording OK");
                    }
                });
                Intent intent = new Intent(this, VideoCatchFrameActivity.class);
                intent.putExtra(VideoCatchFrameActivity.COM_EXAMPLE_HOOLLYZHANG_RECORDDEMO_EXTRA, recordFilename);
                startActivity(intent);
                break;
            case R.id.change_camera:
                mRecordGLSurfaceView.switchCamera();
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        // ...
        initRecordView();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // ...
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.play_video:
                startActivity(new Intent(this, RecyclerViewVideoActivity.class));
                break;
            case R.id.play_video2:
                startActivity(new Intent(this, VideoPlayerActivity.class));
                break;
            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
