package io.github.necrashter.natural_revenge.mod;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

import io.github.necrashter.natural_revenge.Main;

/**
 * Floating mod menu UI with a draggable button and toggleable menu panel.
 */
public class ModMenuUI {
    private final Stage stage;
    private final Skin skin;
    private final ModConfig config;

    private Table floatingButton;
    private Window menuWindow;
    private boolean menuVisible = false;

    // Textures for the floating button
    private Texture buttonTexture;

    public ModMenuUI(Stage stage) {
        this.stage = stage;
        this.skin = Main.skin;
        this.config = ModConfig.getInstance();

        createFloatingButton();
        createMenuWindow();
    }

    private void createFloatingButton() {
        // Create a simple colored button texture
        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);

        // Draw outer circle (border)
        pixmap.setColor(new Color(0.2f, 0.8f, 0.2f, 1f));
        fillCircle(pixmap, 32, 32, 30);

        // Draw inner circle
        pixmap.setColor(new Color(0.1f, 0.5f, 0.1f, 1f));
        fillCircle(pixmap, 32, 32, 25);

        // Draw "M" letter for Mod
        pixmap.setColor(Color.WHITE);
        // Simple M shape
        pixmap.fillRectangle(16, 18, 4, 28);  // Left vertical
        pixmap.fillRectangle(44, 18, 4, 28);  // Right vertical
        pixmap.fillRectangle(20, 18, 6, 4);   // Left top diagonal
        pixmap.fillRectangle(38, 18, 6, 4);   // Right top diagonal
        pixmap.fillRectangle(28, 22, 8, 4);   // Middle

        buttonTexture = new Texture(pixmap);
        pixmap.dispose();

        // Create floating button container
        floatingButton = new Table();
        floatingButton.setBackground(new TextureRegionDrawable(new TextureRegion(buttonTexture)));
        floatingButton.setSize(64, 64);
        floatingButton.setPosition(20, Gdx.graphics.getHeight() / 2f);

        // Make button draggable
        floatingButton.addListener(new DragListener() {
            private float startX, startY;

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                startX = floatingButton.getX();
                startY = floatingButton.getY();
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                floatingButton.moveBy(x - floatingButton.getWidth() / 2, y - floatingButton.getHeight() / 2);

                // Clamp to screen bounds
                float clampedX = Math.max(0, Math.min(floatingButton.getX(), stage.getWidth() - floatingButton.getWidth()));
                float clampedY = Math.max(0, Math.min(floatingButton.getY(), stage.getHeight() - floatingButton.getHeight()));
                floatingButton.setPosition(clampedX, clampedY);
            }
        });

        // Click to toggle menu
        floatingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleMenu();
            }
        });

        stage.addActor(floatingButton);
    }

    private void fillCircle(Pixmap pixmap, int centerX, int centerY, int radius) {
        for (int y = -radius; y <= radius; y++) {
            for (int x = -radius; x <= radius; x++) {
                if (x * x + y * y <= radius * radius) {
                    pixmap.drawPixel(centerX + x, centerY + y);
                }
            }
        }
    }

    private void createMenuWindow() {
        menuWindow = new Window("Mod Menu", skin);
        menuWindow.setSize(300, 400);
        menuWindow.setPosition(100, Gdx.graphics.getHeight() / 2f - 200);
        menuWindow.setMovable(true);
        menuWindow.setResizable(false);

        // Make window draggable by title bar
        menuWindow.getTitleTable().addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                menuWindow.moveBy(x - menuWindow.getWidth() / 2, y);
            }
        });

        Table content = new Table();
        content.pad(10);
        content.defaults().left().padBottom(8);

        // Title
        Label titleLabel = new Label("-- CHEATS --", skin);
        titleLabel.setColor(Color.YELLOW);
        content.add(titleLabel).center().colspan(1).padBottom(15).row();

        // Bunnyhop toggle
        content.add(createToggle("Bunnyhop", config.bunnyhop, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.bunnyhop = ((CheckBox) actor).isChecked();
            }
        })).row();

        // AirStrafe toggle
        content.add(createToggle("AirStrafe", config.airStrafe, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.airStrafe = ((CheckBox) actor).isChecked();
            }
        })).row();

        // Third Person toggle
        content.add(createToggle("Third Person", config.thirdPerson, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.thirdPerson = ((CheckBox) actor).isChecked();
            }
        })).row();

        // Rapid Fire toggle
        content.add(createToggle("Rapid Fire", config.rapidFire, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.rapidFire = ((CheckBox) actor).isChecked();
            }
        })).row();

        // Infinite Ammo toggle
        content.add(createToggle("Infinite Ammo", config.infiniteAmmo, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.infiniteAmmo = ((CheckBox) actor).isChecked();
            }
        })).row();

        // One Hit Kill toggle
        content.add(createToggle("One Hit Kill", config.oneHitKill, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.oneHitKill = ((CheckBox) actor).isChecked();
            }
        })).row();

        // Spacer
        content.add().expandY().row();

        // Close button
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hideMenu();
            }
        });
        content.add(closeButton).center().padTop(10).row();

        menuWindow.add(content).expand().fill();
        menuWindow.setVisible(false);
        stage.addActor(menuWindow);
    }

    private CheckBox createToggle(String label, boolean initialState, ChangeListener listener) {
        CheckBox checkBox = new CheckBox(" " + label, skin);
        checkBox.setChecked(initialState);
        checkBox.addListener(listener);
        checkBox.getLabel().setColor(Color.WHITE);
        return checkBox;
    }

    public void toggleMenu() {
        if (menuVisible) {
            hideMenu();
        } else {
            showMenu();
        }
    }

    public void showMenu() {
        menuVisible = true;
        menuWindow.setVisible(true);

        // Update checkbox states from config
        updateCheckboxStates();

        // Position menu next to floating button
        float menuX = floatingButton.getX() + floatingButton.getWidth() + 10;
        float menuY = floatingButton.getY() + floatingButton.getHeight() / 2 - menuWindow.getHeight() / 2;

        // Clamp to screen
        if (menuX + menuWindow.getWidth() > stage.getWidth()) {
            menuX = floatingButton.getX() - menuWindow.getWidth() - 10;
        }
        menuY = Math.max(0, Math.min(menuY, stage.getHeight() - menuWindow.getHeight()));

        menuWindow.setPosition(menuX, menuY);
    }

    public void hideMenu() {
        menuVisible = false;
        menuWindow.setVisible(false);
    }

    private void updateCheckboxStates() {
        // This could be enhanced to sync UI with config state
    }

    public boolean isMenuVisible() {
        return menuVisible;
    }

    /**
     * Call this to bring the mod button to the front (useful after screen resize)
     */
    public void toFront() {
        floatingButton.toFront();
        if (menuVisible) {
            menuWindow.toFront();
        }
    }

    public void dispose() {
        if (buttonTexture != null) {
            buttonTexture.dispose();
        }
    }

    /**
     * Update floating button position after screen resize
     */
    public void resize(int width, int height) {
        // Clamp button position to new screen bounds
        float clampedX = Math.min(floatingButton.getX(), width - floatingButton.getWidth());
        float clampedY = Math.min(floatingButton.getY(), height - floatingButton.getHeight());
        floatingButton.setPosition(Math.max(0, clampedX), Math.max(0, clampedY));
    }
}
