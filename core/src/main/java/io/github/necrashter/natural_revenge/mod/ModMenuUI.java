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

    private Texture buttonTexture;

    public ModMenuUI(Stage stage) {
        this.stage = stage;
        this.skin = Main.skin;
        this.config = ModConfig.getInstance();

        createFloatingButton();
        createMenuWindow();
    }

    private void createFloatingButton() {
        Pixmap pixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);

        pixmap.setColor(new Color(0.2f, 0.8f, 0.2f, 1f));
        fillCircle(pixmap, 32, 32, 30);

        pixmap.setColor(new Color(0.1f, 0.5f, 0.1f, 1f));
        fillCircle(pixmap, 32, 32, 25);

        pixmap.setColor(Color.WHITE);
        pixmap.fillRectangle(16, 18, 4, 28);
        pixmap.fillRectangle(44, 18, 4, 28);
        pixmap.fillRectangle(20, 18, 6, 4);
        pixmap.fillRectangle(38, 18, 6, 4);
        pixmap.fillRectangle(28, 22, 8, 4);

        buttonTexture = new Texture(pixmap);
        pixmap.dispose();

        floatingButton = new Table();
        floatingButton.setBackground(new TextureRegionDrawable(new TextureRegion(buttonTexture)));
        floatingButton.setSize(64, 64);
        floatingButton.setPosition(20, Gdx.graphics.getHeight() / 2f);

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

                float clampedX = Math.max(0, Math.min(floatingButton.getX(), stage.getWidth() - floatingButton.getWidth()));
                float clampedY = Math.max(0, Math.min(floatingButton.getY(), stage.getHeight() - floatingButton.getHeight()));
                floatingButton.setPosition(clampedX, clampedY);
            }
        });

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

        menuWindow.getTitleTable().addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                menuWindow.moveBy(x - menuWindow.getWidth() / 2, y);
            }
        });

        Table content = new Table();
        content.pad(10);
        content.defaults().left().padBottom(8);

        Label titleLabel = new Label("-- CHEATS --", skin);
        titleLabel.setColor(Color.YELLOW);
        content.add(titleLabel).center().colspan(1).padBottom(15).row();

        content.add(createToggle("Bunnyhop", config.bunnyhop, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.bunnyhop = ((CheckBox) actor).isChecked();
            }
        })).row();

        content.add(createToggle("AirStrafe", config.airStrafe, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.airStrafe = ((CheckBox) actor).isChecked();
            }
        })).row();

        content.add(createToggle("Third Person", config.thirdPerson, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.thirdPerson = ((CheckBox) actor).isChecked();
            }
        })).row();

        content.add(createToggle("Rapid Fire", config.rapidFire, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.rapidFire = ((CheckBox) actor).isChecked();
            }
        })).row();

        content.add(createToggle("Infinite Ammo", config.infiniteAmmo, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.infiniteAmmo = ((CheckBox) actor).isChecked();
            }
        })).row();

        content.add(createToggle("One Hit Kill", config.oneHitKill, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                config.oneHitKill = ((CheckBox) actor).isChecked();
            }
        })).row();

        content.add().expandY().row();

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

        updateCheckboxStates();

        float menuX = floatingButton.getX() + floatingButton.getWidth() + 10;
        float menuY = floatingButton.getY() + floatingButton.getHeight() / 2 - menuWindow.getHeight() / 2;

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
