package io.github.necrashter.natural_revenge.mod;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import io.github.necrashter.natural_revenge.world.GameWorld;
import io.github.necrashter.natural_revenge.world.entities.GameEntity;
import io.github.necrashter.natural_revenge.world.player.Player;

/**
 * Renders ESP (wallhack) overlays for enemies.
 * Shows enemy positions through walls with colored boxes and distance info.
 */
public class ESPRenderer {
    private final ShapeRenderer shapeRenderer;
    private final Vector3 screenPos = new Vector3();
    private final Vector3 screenPosTop = new Vector3();

    public ESPRenderer() {
        shapeRenderer = new ShapeRenderer();
    }

    public void render(GameWorld world, Camera camera) {
        if (!ModConfig.get().enemyESP) return;
        if (world.player == null) return;

        // Disable depth test to render through walls
        Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

        shapeRenderer.setProjectionMatrix(camera.combined);

        // Draw 3D boxes around enemies
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (GameEntity entity : world.octree.entities) {
            // Skip player
            if (entity instanceof Player) continue;

            // Skip dead entities
            if (entity.health <= 0 || entity.dead) continue;

            // Get entity position
            Vector3 pos = entity.hitBox.position;

            // Calculate distance to player
            float dx = pos.x - world.player.hitBox.position.x;
            float dy = pos.y - world.player.hitBox.position.y;
            float dz = pos.z - world.player.hitBox.position.z;
            float distance = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);

            // Skip if too far
            if (distance > 100f) continue;

            // Color based on distance (red = close, yellow = medium, green = far)
            Color color;
            if (distance < 10f) {
                color = Color.RED;
            } else if (distance < 25f) {
                color = Color.ORANGE;
            } else if (distance < 50f) {
                color = Color.YELLOW;
            } else {
                color = Color.GREEN;
            }

            shapeRenderer.setColor(color);

            // Draw box around entity
            float halfWidth = entity.hitBox.radius;
            float height = entity.hitBox.height;

            // Bottom square
            shapeRenderer.line(pos.x - halfWidth, pos.y, pos.z - halfWidth,
                              pos.x + halfWidth, pos.y, pos.z - halfWidth);
            shapeRenderer.line(pos.x + halfWidth, pos.y, pos.z - halfWidth,
                              pos.x + halfWidth, pos.y, pos.z + halfWidth);
            shapeRenderer.line(pos.x + halfWidth, pos.y, pos.z + halfWidth,
                              pos.x - halfWidth, pos.y, pos.z + halfWidth);
            shapeRenderer.line(pos.x - halfWidth, pos.y, pos.z + halfWidth,
                              pos.x - halfWidth, pos.y, pos.z - halfWidth);

            // Top square
            shapeRenderer.line(pos.x - halfWidth, pos.y + height, pos.z - halfWidth,
                              pos.x + halfWidth, pos.y + height, pos.z - halfWidth);
            shapeRenderer.line(pos.x + halfWidth, pos.y + height, pos.z - halfWidth,
                              pos.x + halfWidth, pos.y + height, pos.z + halfWidth);
            shapeRenderer.line(pos.x + halfWidth, pos.y + height, pos.z + halfWidth,
                              pos.x - halfWidth, pos.y + height, pos.z + halfWidth);
            shapeRenderer.line(pos.x - halfWidth, pos.y + height, pos.z + halfWidth,
                              pos.x - halfWidth, pos.y + height, pos.z - halfWidth);

            // Vertical lines
            shapeRenderer.line(pos.x - halfWidth, pos.y, pos.z - halfWidth,
                              pos.x - halfWidth, pos.y + height, pos.z - halfWidth);
            shapeRenderer.line(pos.x + halfWidth, pos.y, pos.z - halfWidth,
                              pos.x + halfWidth, pos.y + height, pos.z - halfWidth);
            shapeRenderer.line(pos.x + halfWidth, pos.y, pos.z + halfWidth,
                              pos.x + halfWidth, pos.y + height, pos.z + halfWidth);
            shapeRenderer.line(pos.x - halfWidth, pos.y, pos.z + halfWidth,
                              pos.x - halfWidth, pos.y + height, pos.z + halfWidth);

            // Draw line from entity to its head (targeting line)
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.line(pos.x, pos.y + height * 0.8f, pos.z,
                              pos.x, pos.y + height + 0.5f, pos.z);
        }

        shapeRenderer.end();

        // Re-enable depth test
        Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
