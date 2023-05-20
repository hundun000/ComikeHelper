package hundun.tool.libgdx.other;

import lombok.Getter;

public class CameraDataPackage {

    @Getter
    private float currentCameraX;
    @Getter
    private float currentCameraY;
    @Getter
    private float currentCameraZoomWeight;
    @Getter
    private boolean currentCameraZoomDirty;

    public void modifyCurrentCamera(Float deltaX, Float deltaY) {
        if (deltaX != null) {
            currentCameraX += deltaX;
        }
        if (deltaY != null) {
            currentCameraY += deltaY;
        }
    }

    public void modifyCurrentCameraZoomWeight(Float delta) {
        currentCameraZoomWeight += delta;
        currentCameraZoomDirty = true;
    }

    public boolean getAndClearCameraZoomDirty () {
        boolean result = currentCameraZoomDirty;
        currentCameraZoomDirty = false;
        return result;
    }

    public void forceSet(float currentCameraX, float currentCameraY, int currentCameraZoomWeight) {
        this.currentCameraX = currentCameraX;
        this.currentCameraY = currentCameraY;
        this.currentCameraZoomWeight = currentCameraZoomWeight;
        this.currentCameraZoomDirty = true;
    }
}
