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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.TimeUtils;

import io.github.necrashter.natural_revenge.Main;
import io.github.necrashter.natural_revenge.world.GameWorld;

/**
 * Floating mod menu UI with a draggable button and toggleable menu panel.
 * Fixed touch handling for Android - uses combined touch detection for both drag and tap.
 */
public class ModMenuUI {
    private final Stage stage;
    private final Skin skin;
    private final ModConfig config;
    private final GameWorld world;

    private Image floatingButton;
    private Window menuWindow;
    private boolean menuVisible = false;
    private boolean wasGamePaused = false;

    // Textures for the floating button
    private Texture buttonTexture;

    // Touch tracking for drag vs tap detection
    private float touchStartX, touchStartY;
    private float buttonStartX, buttonStartY;
    private long touchStartTime;
    private boolean isDragging = false;
    private static final float DRAG_THRESHOLD = 10f;
    private static final long TAP_TIME_THRESHOLD = 300;

    public ModMenuUI(Stage stage, GameWorld world) {
        this.stage = stage;
        this.skin = Main.skin;
        this.config = ModConfig.getInstance();
        this.world = world;

        createFloatingButton();
        createMenuWindow();
    }

    private void createFloatingButton() {
        Pixmap pixmap = new Pixmap(80, 80, Pixmap.Format.RGBA8888);

        // Draw outer circle (border)
        pixmap.setColor(new Color(0.2f, 0.8f, 0.2f, 0.9f));
        fillCircle(pixmap, 40, 40, 38);

        // Draw inner circle
        pixmap.setColor(new Color(0.1f, 0.5f, 0.1f, 0.9f));
        fillCircle(pixmap, 40, 40, 32);

        // Draw "M" letter for Mod
        pixmap.setColor(Color.WHITE);
        pixmap.fillRectangle(18, 20, 6, 40);
        pixmap.fillRectangle(56, 20, 6, 40);
        pixmap.fillRectangle(24, 20, 8, 6);
        pixmap.fillRectangle(48, 20, 8, 6);
        pixmap.fillRectangle(32, 26, 16, 6);

        buttonTexture = new Texture(pixmap);
        pixmap.dispose();

        floatingButton = new Image(new TextureRegionDrawable(new TextureRegion(buttonTexture)));
        floatingButton.setSize(80, 80);
        floatingButton.setPosition(20, Gdx.graphics.getHeight() / 2f);
        floatingButton.setTouchable(Touchable.enabled);

        floatingButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchStartX = x;
                touchStartY = y;
                buttonStartX = floatingButton.getX();
                buttonStartY = floatingButton.getY();
                touchStartTime = TimeUtils.millis();
                isDragging = false;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                float deltaX = x - touchStartX;
                float deltaY = y - touchStartY;

                if (Math.abs(deltaX) > DRAG_THRESHOLD || Math.abs(deltaY) > DRAG_THRESHOLD) {
                    isDragging = true;
                }

                if (isDragging) {
                    float newX = buttonStartX + deltaX;
                    float newY = buttonStartY + deltaY;

                    newX = Math.max(0, Math.min(newX, stage.getWidth() - floatingButton.getWidth()));
                    newY = Math.max(0, Math.min(newY, stage.getHeight() - floatingButton.getHeight()));

                    floatingButton.setPosition(newX, newY);
                    buttonStartX = newX;
                    buttonStartY = newY;
                    touchStartX = x;
                    touchStartY = y;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                long touchDuration = TimeUtils.millis() - touchStartTime;

                if (!isDragging && touchDuration < TAP_TIME_THRESHOLD) {
                    toggleMenu();
                }
                isDragging = false;
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
        menuWindow.setSize(320, 520);
        menuWindow.setPosition(120, Gdx.graphics.getHeight() / 2f - 260);
        menuWindow.setMovable(true);
        menuWindow.setResizable(false);
        menuWindow.setKeepWithinStage(true);

        Table content = new Table();
        content.pad(10);
        content.defaults().left().padBottom(8);

        // Title
        Label titleLabel = new Label("-- CHEATS --", skin);
        titleLabel.setColor(Color.YELLOW);
        content.add(titleLabel).center().colspan(1).padBottom(15).row();

        // God Mode
        content.add(createToggle("God Mode", config.godMode, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.godMode = ((CheckBox) actor).isChecked();
            }
        })).row();

        // Silent Aimbot
        content.add(createToggle("Silent Aimbot", config.silentAimbot, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.silentAimbot = ((CheckBox) actor).isChecked();
            }
        })).row();

        // ESP / Wallhack
        content.add(createToggle("Enemy ESP", config.enemyESP, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.enemyESP = ((CheckBox) actor).isChecked();
            }
        })).row();

        // One Hit Kill
        content.add(createToggle("One Hit Kill", config.oneHitKill, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.oneHitKill = ((CheckBox) actor).isChecked();
            }
        })).row();

        // Infinite Ammo
        content.add(createToggle("Infinite Ammo", config.infiniteAmmo, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.infiniteAmmo = ((CheckBox) actor).isChecked();
            }
        })).row();

        // Rapid Fire
        content.add(createToggle("Rapid Fire", config.rapidFire, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.rapidFire = ((CheckBox) actor).isChecked();
            }
        })).row();

        // No Recoil
        content.add(createToggle("No Recoil", config.noRecoil, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.noRecoil = ((CheckBox) actor).isChecked();
            }
        })).row();

        // Bunnyhop
        content.add(createToggle("Bunnyhop", config.bunnyhop, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.bunnyhop = ((CheckBox) actor).isChecked();
            }
        })).row();

        // AirStrafe
        content.add(createToggle("AirStrafe", config.airStrafe, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.airStrafe = ((CheckBox) actor).isChecked();
            }
        })).row();

        // Third Person
        content.add(createToggle("Third Person", config.thirdPerson, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.thirdPerson = ((CheckBox) actor).isChecked();
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
        content.add(closeButton).center().padTop(10).width(120).height(45).row();

        menuWindow.add(content).expand().fill();
        menuWindow.setVisible(false);
        stage.addActor(menuWindow);
    }

    private CheckBox createToggle(String label, boolean initialState, ChangeListener listener) {
        CheckBox checkBox = new CheckBox(" " + label, skin);
        checkBox.setChecked(initialState);
        checkBox.addListener(listener);
        checkBox.getLabel().setColor(Color.WHITE);
        checkBox.left();
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
        menuWindow.toFront();

        // Pause game when menu opens
        wasGamePaused = world.paused;
        world.paused = true;

        // Position menu next to floating button
        float menuX = floatingButton.getX() + floatingButton.getWidth() + 15;
        float menuY = floatingButton.getY() + floatingButton.getHeight() / 2 - menuWindow.getHeight() / 2;

        if (menuX + menuWindow.getWidth() > stage.getWidth()) {
            menuX = floatingButton.getX() - menuWindow.getWidth() - 15;
        }
        if (menuX < 0) menuX = 10;
        menuY = Math.max(10, Math.min(menuY, stage.getHeight() - menuWindow.getHeight() - 10));

        menuWindow.setPosition(menuX, menuY);
    }

    public void hideMenu() {
        menuVisible = false;
        menuWindow.setVisible(false);

        // Restore game pause state
        if (!wasGamePaused) {
            world.paused = false;
        }
    }

    public boolean isMenuVisible() {
        return menuVisible;
    }

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

    public void resize(int width, int height) {
        float clampedX = Math.min(floatingButton.getX(), width - floatingButton.getWidth());
        float clampedY = Math.min(floatingButton.getY(), height - floatingButton.getHeight());
        floatingButton.setPosition(Math.max(0, clampedX), Math.max(0, clampedY));
    }
}
