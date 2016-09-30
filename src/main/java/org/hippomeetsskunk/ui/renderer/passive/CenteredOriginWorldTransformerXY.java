package org.hippomeetsskunk.ui.renderer.passive;

import org.hippomeetsskunk.physics.basics.Vector3;

/**
 * Created by SRZCHX on 30.09.2016.
 */
public class CenteredOriginWorldTransformerXY implements WorldToDisplayTransformer {
    private final double worldScaleX;
    private final double worldScaleY;

    public CenteredOriginWorldTransformerXY(double worldScaleX, double worldScaleY) {
        this.worldScaleX = worldScaleX;
        this.worldScaleY = worldScaleY;
    }

    @Override
    public int getX(Vector3 position, int drawingWidth) {
        return (int) ((1.0 + position.getX() / worldScaleX) * drawingWidth * 0.5);
    }

    @Override
    public int getY(Vector3 position, int drawingHeight) {
        return (int) ((1.0 + position.getY() / worldScaleY) * drawingHeight * 0.5);
    }

}
