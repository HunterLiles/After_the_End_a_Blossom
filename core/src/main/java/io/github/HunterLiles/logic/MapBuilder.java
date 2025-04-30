package io.github.HunterLiles.logic;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.Gdx;

public class MapBuilder {
    public static void buildShapes(TiledMap map, World world) {
        if (map == null) {
            throw new IllegalArgumentException("TiledMap is null");
        }
        if (world == null) {
            throw new IllegalArgumentException("World is null");
        }

        // Get the foreground layer (index 1) and verify it exists
        MapLayer foregroundLayer = map.getLayers().get("Foreground");
        if (foregroundLayer == null) {
            throw new RuntimeException("No 'Foreground' layer found in map");
        }

        if (!(foregroundLayer instanceof TiledMapTileLayer)) {
            throw new RuntimeException("Foreground layer is not a TiledMapTileLayer");
        }

        TiledMapTileLayer layer = (TiledMapTileLayer) foregroundLayer;
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        float tileWidth = layer.getTileWidth();
        float tileHeight = layer.getTileHeight();

        // Create bodies for tiles
        for (int y = 0; y < layer.getHeight(); y++) {
            for (int x = 0; x < layer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);

                if (cell == null || cell.getTile() == null) {
                    continue;
                }

                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(
                    (x * tileWidth + tileWidth/2) / PhysicsConstants.PPM,
                    (y * tileHeight + tileHeight/2) / PhysicsConstants.PPM
                );

                Body body = world.createBody(bodyDef);

                PolygonShape shape = new PolygonShape();
                try {
                    shape.setAsBox(
                        (tileWidth / 2) / PhysicsConstants.PPM,
                        (tileHeight / 2) / PhysicsConstants.PPM
                    );

                    fixtureDef.shape = shape;
                    fixtureDef.friction = 0.4f;
                    fixtureDef.filter.categoryBits = PhysicsConstants.CATEGORY_GROUND;

                    body.createFixture(fixtureDef);
                } finally {
                    shape.dispose();
                }
            }
        }

        // Handle objects layer
        try {
            MapLayer objectsLayer = map.getLayers().get("Objects");
            if (objectsLayer == null) {
                Gdx.app.debug("MapBuilder", "No 'Objects' layer found in map");
                return;
            }

            MapObjects objects = objectsLayer.getObjects();
            if (objects != null) {
                for (MapObject object : objects) {
                    if (object instanceof RectangleMapObject) {
                        Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                        bodyDef.type = BodyDef.BodyType.StaticBody;
                        bodyDef.position.set(
                            (rectangle.x + rectangle.width / 2) / PhysicsConstants.PPM,
                            (rectangle.y + rectangle.height / 2) / PhysicsConstants.PPM
                        );

                        Body body = world.createBody(bodyDef);
                        PolygonShape shape = new PolygonShape();
                        try {
                            shape.setAsBox(
                                (rectangle.width / 2) / PhysicsConstants.PPM,
                                (rectangle.height / 2) / PhysicsConstants.PPM
                            );

                            fixtureDef.shape = shape;
                            fixtureDef.friction = 0.4f;
                            fixtureDef.filter.categoryBits = PhysicsConstants.CATEGORY_PLAYER;

                            body.createFixture(fixtureDef);
                        } finally {
                            shape.dispose();
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error processing objects layer: " + e.getMessage(), e);
        }
    }
}
