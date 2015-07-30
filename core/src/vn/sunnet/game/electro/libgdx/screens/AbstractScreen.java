package vn.sunnet.game.electro.libgdx.screens;

import vn.sunnet.game.electro.libgdx.scene2d.ui.SpriteSheet;

import vn.sunnet.game.electro.libgdx.screens.Toast.ToastStyle;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.PauseableThread;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public abstract class AbstractScreen implements Screen {
	
	protected Game game;
	protected InputProcessor preProcessor;
	protected Stage uiStage;
	
	public static Skin loadingSkin = null;
	public static TextureAtlas loadingAtlas = null;
	
	public static final String FONT_CHARACTERS = "AÁÀẢÃẠĂẮẰẲẴẶÂẤẦẨẪẬEÉÈẺẼẸÊẾỀỂỄỆIÍÌỈĨỊOÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢYÝỲỶỸỴUÚÙỦŨỤƯỨỪỬỮỰBCDĐFGHJKLMNPQRSTVWXZaáàảãạăắằẳẵặâấầẩẫậeéèẻẽẹêếềểễệiíìỉĩịoóòỏõọôốồổỗộơớờởỡợyýỳỷỹỵuúùủũụưứừửữựbcdđfghjklmnpqrstvwxz0123456789!?,.:;'$%&()-+=/|\"";
//	public static FileHandle fontFile = Gdx.files.internal("electro/boldfont.otf");
	public static BitmapFont dialogFont = null;
	public static Texture dialogBody = null;
	public static Texture dialogTop = null;
	public static Texture emptyButton = null;
	
	protected LoadingDialog loadingDialog;
	protected LabelStyle labelStyle;
	protected WindowStyle windowStyle;
	protected ToastStyle toastStyle;
	protected TextButtonStyle buttonStyle;
	private Toast toast;
	private MyDialog dialog;
	
	protected boolean firstTime = true; 
	
	
	public AbstractScreen(int width, int height, Game game) {
		// TODO Auto-generated constructor stub
		this.game = game;
		uiStage = new Stage(new StretchViewport(width, height));
		firstTime = true;
		create();
	}
	
	public void create() {
		loadAssets();
		initUIStage();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		unloadAssets();
		uiStage.dispose();
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		uiStage.act(delta);
		uiStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		/*Libgdx automatically reload context of OpengGl*/
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
	}
	
	public void setProcessorAfterDismissingLoading(InputProcessor inputProcessor) {
		preProcessor = inputProcessor;
		System.out.println("Da luu truoc processor truoc do");
	}
	
	public boolean isLoadingShowed() {
		return loadingDialog.getParent() != null;
//		return loadingSlogan.isVisible();
	}
	
	public void setLoadingSlogan(final String slogan, final Runnable handleWhenTimeOut, final int type, final float timeout) {
		Gdx.input.setInputProcessor(uiStage);
		Gdx.app.postRunnable(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				loadingDialog.show(slogan, uiStage, handleWhenTimeOut, type, timeout);
				
			}
		});
	}
	
	/**
	 * show the excutive electro progress with the slogan
	 * @param slogan The message goes with the loading
	 */
	public void showLoadingDialog(final String slogan, final Runnable handleWhenTimeOut, final int type, final float timeout) {
		Gdx.input.setInputProcessor(uiStage);
		Gdx.app.postRunnable(new Runnable() {
			
			@Override
			public void run() {
				loadingDialog.show(slogan, uiStage, handleWhenTimeOut, type, timeout);
				
			}
		});
		
	}
	
	/**
	 * show the excutive electro progress with the default slogan
	 */
	public void showLoadingDialog(final Runnable handleWhenTimeOut, final int type, final float timeout) {
		Gdx.input.setInputProcessor(uiStage);
		Gdx.app.postRunnable(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				loadingDialog.show("Đang tải", uiStage, handleWhenTimeOut, type, timeout);
				
			}
		});
	}
	
	/**
	 * hide the excutive electro progress
	 * @param inputProcessor the inputprocessor will receive input after dismissing loadingDialog
	 */
	public void dismissLoadingDialog(final InputProcessor inputProcessor) {
		Gdx.input.setInputProcessor(inputProcessor);
		Gdx.app.postRunnable(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				loadingDialog.hide();
				
			}
		});
	}
	
	/**
	 * hide the excutive electro progress
	 */
	public void dismissLoadingDialog() {
		Gdx.input.setInputProcessor(preProcessor);
		Gdx.app.postRunnable(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				loadingDialog.hide();
				
				
			}
		});
	}
	
	/**
	 * 
	 * @param slogan string of context
	 * @param duration amount of time  the slogan appears for
	 */
	public void createToast(final String slogan, final float duration) {
		Gdx.app.postRunnable(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("show Toast"+ slogan);
				if (toast != null) toast.remove();
				toast = new Toast(slogan, duration, false, toastStyle);
				toast.show(uiStage);
			}
		});
	}
	
	public void createToast(final String slogan, final float duration, final Runnable complete) {
		Gdx.app.postRunnable(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("show Toast"+ slogan);
				if (toast != null) toast.remove();
				toast = new Toast(slogan, duration, false, toastStyle, complete);
				toast.show(uiStage);
			}
		});
	}
	
	
	public void createDialog(final String title, final String context,
			final ButtonDescription positive, final ButtonDescription negative,
			final ButtonDescription neutral, final InputProcessor toInputProcessor) {
		if (dialog != null)
			if (dialog.getParent() != null) {
				System.out.println("Truoc da co diloag");
				return;
			}
		System.out.println("Truoc tao dialog");
		Gdx.input.setInputProcessor(uiStage);
		Gdx.app.postRunnable(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final Command command = new Command() {
					
					@Override
					public void execute(Object data) {
						// TODO Auto-generated method stub
						
						if (toInputProcessor == null) {
							Gdx.input.setInputProcessor(preProcessor);
							System.out.println("quay tro ve man hinh chinh");
						}
						else {
							Gdx.input.setInputProcessor(toInputProcessor);
							System.out.println("quay tro ve man hinh khac");
						}
						
					}
				};
				dialog =  new MyDialog(title, windowStyle) {
					protected void result (Object object) {
						System.out.println("Chosen: " + object);
						command.execute(null);
						if ((Integer)object == 0) {
							negative.excute();
						} else if ((Integer)object == 1) {
							positive.excute();
						} else if ((Integer)object == 2) {
							neutral.excute();
						}
					}
				}.setShow(uiStage);
				dialog = dialog.text(context, labelStyle);
				if (positive != null)
					dialog = dialog.button(positive.name(), 1, buttonStyle);
				if (neutral != null)
					dialog = dialog.button(neutral.name(), 2, buttonStyle);
				if (negative != null)
					dialog = dialog.button(negative.name(), 0, buttonStyle);
				dialog = dialog.key(Keys.ESCAPE, 2).show();
			}
		});
	}
	
	public void initUIStage() {
		uiStage.clear();
		
//		loadingDialog = new LoadingDialog(loadingAtlas.findRegions("Loading"), loadingSkin);
		
		NinePatch patch = new NinePatch(dialogBody, 15, 15, 15, 15);
		
		toastStyle = new ToastStyle(new NinePatchDrawable(patch),
				new NinePatchDrawable(patch), new NinePatchDrawable(patch),
				dialogFont);
		
		windowStyle = new WindowStyle(dialogFont, dialogFont.getColor(),
				new NinePatchDrawable(patch));
		
		labelStyle = new LabelStyle(dialogFont, dialogFont.getColor());
		
		patch = new NinePatch(emptyButton, 0 , 0, 0, 0);
		
		buttonStyle = new TextButtonStyle(new NinePatchDrawable(patch),
				new NinePatchDrawable(patch), new NinePatchDrawable(patch),
				dialogFont);
		
	}
	
	private void loadAssets() {
//		if (loadingAtlas == null) {
//			System.out.println("loatAtlas");
//			loadingAtlas = new TextureAtlas(Gdx.files.internal("electro/Loading.pack"));
//		}
//		if (loadingSkin == null) {
//			loadingSkin = new Skin(Gdx.files.internal("electro/uiskin.json"));
//		}
		if (dialogFont == null) {
			dialogFont = new BitmapFont(
					Gdx.files.internal("electro/new/DialogFont.fnt"),
					Gdx.files.internal("electro/new/DialogFont.png"), false);
			dialogFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		if (dialogBody == null) {
			dialogBody = new Texture(Gdx.files.internal("electro/new/DialogBody.png"));
		}
		
		if (dialogTop == null) {
			dialogTop = new Texture(Gdx.files.internal("electro/new/DialogTop.png"));
		}
		
		if (emptyButton == null) {
			emptyButton = new Texture(Gdx.files.internal("electro/new/EmptyButton.png"));
		}
	
	}
	
	private void unloadAssets() {
		if (loadingAtlas != null)
			loadingAtlas.dispose();
		if (loadingSkin != null)
			loadingSkin.dispose();
	}
	
	public abstract InputProcessor getInputProcessor();
	
	public abstract void playMusic();
	
	public abstract void pauseMusic();
	
	public interface Command {
		public void execute(Object data);
	}
}
