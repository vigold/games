package vn.sunnet.game.electro.libgdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.sun.corba.se.spi.orbutil.fsm.State;
import com.sun.xml.internal.messaging.saaj.packaging.mime.Header;

public class Toast extends Group{


	private TextButton toast;	
	private boolean isCanDestroy;
	private ToastStyle style = null;
	private String text;
	private float duration;
	Runnable complete = null;
	
	public Toast(String text, float time, boolean isCanTouchToRemove, ToastStyle style) {
		this.style = style;
		this.text = text;
		this.duration = time;
	}
	
	public Toast(String text, float time, boolean isCanTouchToRemove, ToastStyle style, Runnable complete) {
		this.style = style;
		this.text = text;
		this.duration = time;
		this.complete = complete;
	}
	
	public void show(Stage stage) {
		stage.addActor(this);
		
		LabelStyle style = new LabelStyle(this.style.font, this.style.fontColor);
		Label label = new Label(text, style);
		Image background = new Image(this.style.checked);
		BitmapFont font = this.style.font;
		TextBounds bounds = font.getBounds(text);
		float width = Math.min(bounds.width, 3 * stage.getWidth() / 2);
		
		stage.getBatch().begin();
		bounds = font.drawWrapped(stage.getBatch(), text, 0, 0, width);
		stage.getBatch().end();
		System.out.println(bounds.width+" "+bounds.height);
		
		float height = bounds.height;
		
		
		background.setBounds(0, 0, width + this.style.font.getLineHeight(),
				height + this.style.font.getLineHeight());
		label.setAlignment(Align.center, Align.center);
		float deltaY = (font.getAscent() < 0 ? -font.getAscent() : 0)
				+ font.getLineHeight()
				- (font.getCapHeight());
		label.setBounds(background.getWidth() / 2 - width / 2,
				background.getHeight() / 2 - height / 2 + deltaY, width, height);
		label.setWrap(true);
		
		addActor(background);
		addActor(label);
		
		width = background.getWidth();
		height = background.getHeight();
		setBounds(stage.getWidth() / 2 - width / 2, stage.getHeight() / 2
				- height / 2, width, height);
		setOrigin(getWidth() / 2, getHeight() / 2);
		setTouchable(Touchable.disabled);
		setTransform(true);
		setColor(1f, 1f, 1f, 0);
		addAction(Actions.fadeIn(0.3f, Interpolation.bounceOut));//(1f, 1f, 0.3f,Interpolation.bounceOut));
		System.out.println("ansent"+this.style.font.getAscent());
		System.out.println("descent"+this.style.font.getDescent());
		System.out.println("capHeight"+this.style.font.getCapHeight());
		System.out.println("lineHeight"+this.style.font.getLineHeight());
		System.out.println("xheight"+this.style.font.getXHeight());
		SpriteBatch batch = new SpriteBatch();
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		duration = Math.max(0, duration - delta);
		if ((isCanDestroy && Gdx.input.isTouched()) || (duration == 0)) {
			addAction(Actions.sequence(
					Actions.fadeOut(0.3f, Interpolation.bounceIn),
					new Action() {
						
						@Override
						public boolean act(float delta) {
							if (complete != null) complete.run();
							return true;
						}
					}, Actions.removeActor()));
		}
	}
	
	/** The style for a window, see {@link Window}.
	 * @author Nathan Sweet */
	static public class ToastStyle extends TextButtonStyle {
		public ToastStyle () {
			super();
		}

		public ToastStyle (Drawable up, Drawable down, Drawable checked, BitmapFont font) {
			super(up, down, checked, font);
			this.font = font;
		}

		public ToastStyle (ToastStyle style) {
			super(style);
		}
	}
	
}
