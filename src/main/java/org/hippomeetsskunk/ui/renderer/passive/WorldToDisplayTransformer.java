package org.hippomeetsskunk.ui.renderer.passive;

import org.hippomeetsskunk.physics.basics.Vector3;

/**
 * Created by SRZCHX on 29.09.2016.
 */
public interface WorldToDisplayTransformer {
    int getX(Vector3 position, int drawingWidth);
    int getY(Vector3 position, int drawingHeight);
}
