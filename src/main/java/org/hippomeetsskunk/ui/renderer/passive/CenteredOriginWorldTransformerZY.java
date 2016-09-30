package org.hippomeetsskunk.ui.renderer.passive;

import org.hippomeetsskunk.physics.basics.Vector3;

/**
 * Created by SRZCHX on 30.09.2016.
 */
public class CenteredOriginWorldTransformerZY implements WorldToDisplayTransformer {
    private final double worldScale;

    public CenteredOriginWorldTransformerZY(double worldScale) {
        this.worldScale = worldScale;
    }

    @Override
    public int getX(Vector3 position, int drawingWidth) {
        return (int) (drawingWidth * 0.5 + worldScale * position.getZ());
    }

    @Override
    public int getY(Vector3 position, int drawingHeight) {
        return (int) (drawingHeight *0.5 + worldScale * position.getY());
    }

}
