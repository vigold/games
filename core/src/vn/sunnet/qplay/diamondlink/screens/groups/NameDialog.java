package vn.sunnet.qplay.diamondlink.screens.groups;

import vn.sunnet.game.electro.libgdx.screens.AbstractScreen;
import vn.sunnet.game.electro.libgdx.screens.NodeScreen;
import vn.sunnet.qplay.diamondlink.DiamondLink;
import vn.sunnet.qplay.diamondlink.assets.SoundAssets;
import vn.sunnet.qplay.diamondlink.assets.UIAssets;
import vn.sunnet.qplay.diamondlink.gameobjects.PlayerInfo;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class NameDialog extends AbstractGroup {

	private Label named;
	private TextField nickName;
	private ImageButton ok;
	private Sound press;
	private Runnable okCallback;
	private BitmapFont provinceFont;

	public NameDialog(DiamondLink game, AbstractScreen screen, Runnable okCallback) {
		super(game, screen);
		this.okCallback = okCallback;
		initPrivateAssets();
		initContent();
	}

	@Override
	public void reset() {
		nickName.setText("");
	}

	@Override
	public void show(Group father) {
		father.addActor(this);
	}

	@Override
	public void hide() {
		remove();
		provinceFont.dispose();
	}

	@Override
	protected void initContent() {
	
		
		
		BitmapFont nameFont = assets.getBitmapFont(UIAssets.RANK_NAME_FONT);
		
		setBounds(0, 0, DiamondLink.WIDTH, DiamondLink.HEIGHT);
		
		Texture texture = manager.get(UIAssets.LOGIN_LAYER_0, Texture.class);
		Image image = new Image(texture);
		addActor(image);
		
		texture = manager.get(UIAssets.LOGIN_LAYER_1, Texture.class);
		final Image im = new Image(texture);
		im.addAction(Actions.repeat(-1, Actions.sequence(new Action() {
			
			@Override
			public boolean act(float arg0) {
				im.setBounds(240 - im.getPrefWidth() / 2, -im.getPrefHeight(), im.getPrefWidth(), im.getPrefHeight());
				return true;
			}
		}, Actions.moveTo(0, 800, 15, Interpolation.linear))));
		addActor(im);
		
		TextureAtlas atlas = manager.get(UIAssets.LOGIN_FG, TextureAtlas.class);
		
		

		Group group = new Group();
		group.setBounds(28, 294, 425, 179);
		
		Image bg = new Image(atlas.findRegion("NamedFrame"));
		group.addActor(bg);
		
//		TextureRegionDrawable background = new TextureRegionDrawable(atlas.findRegion("TextField"));
		TextureAtlas atlas2 = manager.get(UIAssets.GAME_FG, TextureAtlas.class);
		TextureRegionDrawable cursor = new TextureRegionDrawable(atlas2.findRegion("Split"));
		TextureRegionDrawable selection = new TextureRegionDrawable(atlas2.findRegion("Split"));
		TextFieldStyle textFieldStyle = new TextFieldStyle(
				nameFont,
				nameFont.getColor(),
				cursor,
				cursor, null);
		
		nickName = new TextField("", textFieldStyle);
		nickName.setTextFieldListener(new TextFieldListener() {
			public void keyTyped (TextField textField, char key) {
				if (key == '\n') textField.getOnscreenKeyboard().show(false);
			}
		});
		
		
		
		nickName.setTextFieldFilter(new TextFieldFilter() {

			@Override
			public boolean acceptChar(TextField textField, char key) {
				// TODO Auto-generated method stub
				return (Character.isLetterOrDigit(key) || key == ' ') && (key != ')')
						&& (key != '(')
						&& (key != ':')
						&& (key != '=')
						&& (key != '?')
						&& (key != '!')
						&& (key != '>')
						&& (key != '<')
						&& (key != '.')
						&& (key != '-')
						&& (key != '_')
						&& (key != '*')
						&& (textField.getText().length() < 10);
			}});
		
		nickName.setBounds(16 + 391f / 2 - 3 * 391f / 8, 23, 3 * 391f / 4, 66);
		group.addActor(nickName);
		
		ok = new ImageButton(new TextureRegionDrawable(
				atlas.findRegion("Ok", 0)), new TextureRegionDrawable(
				atlas.findRegion("Ok", 1)));
		ok.setBounds(205, 195, ok.getPrefWidth(), ok.getPrefHeight());
		ok.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO Auto-generated method stub
				if (DiamondLink.SOUND == 1)
					press.play(1f);
				return true;
			}
			
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				String str = new String(nickName.getText());
				if (nickName.getText().length() == 0 || str.trim().length() == 0) {
					screen.createToast("Your name's at least one letters", 2f);
					return;
				}
				PlayerInfo.offlineName = nickName.getText();
				game.iFunctions.putString("name", PlayerInfo.offlineName);
				if (okCallback != null) okCallback.run();
				hide();
			}
		});
	
		addActor(ok);
		addActor(group);
	}

	@Override
	protected void initPrivateAssets() {
		press = manager.get(SoundAssets.PRESS_SOUND, Sound.class);
		FreeTypeFontGenerator generator = manager.get(UIAssets.DUC_GENERATOR, FreeTypeFontGenerator.class);
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 30;
		provinceFont = generator.generateFont(parameter);
//		provinceFont = generator.generateFont(30, AbstractScreen.FONT_CHARACTERS, false);
		provinceFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}

}
