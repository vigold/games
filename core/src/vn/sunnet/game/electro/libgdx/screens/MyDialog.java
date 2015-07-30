package vn.sunnet.game.electro.libgdx.screens;

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/



import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.ObjectMap;
import com.sun.org.apache.bcel.internal.generic.BALOAD;

/** Displays a dialog, which is a modal window containing a content table with a button table underneath it. Methods are provided
 * to add a label to the content table and buttons to the button table, but any widgets can be added. When a button is clicked,
 * {@link #result(Object)} is called and the dialog is removed from the stage.
 * @author Nathan Sweet */
public class MyDialog extends Dialog {
	/** The time in seconds that dialogs will fade in and out. Set to zero to disable fading. */
	
	private Stage stage;

	

	public MyDialog (String title, Skin skin) {
		super(title, skin.get(WindowStyle.class));
	}

	public MyDialog (String title, Skin skin, String windowStyleName) {
		super(title, skin.get(windowStyleName, WindowStyle.class));
	}

	public MyDialog (String title, WindowStyle windowStyle) {
		super(title, windowStyle);
	}

	/** Adds the given Label to the content table */
	public MyDialog text (Label label) {
		String text = label.getText().toString();
		System.out.println(text);
		
		LabelStyle style = label.getStyle();
		
		TextBounds bounds = label.getStyle().font.getBounds(text);
		float width = Math.min(bounds.width, 3 * stage.getWidth() / 4);
		
		System.out.println(width+" "+bounds.width+" "+stage.getWidth());
		
		stage.getBatch().begin();
		bounds = label.getStyle().font.drawWrapped(stage.getBatch(), text, 0, 0, width);
		stage.getBatch().end();
		
		width = bounds.width;
		float height = bounds.height;
		
		System.out.println(width);
		System.out.println(height);
		
		Group group = new Group();
		
		group.setBounds(0, 0, width + style.font.getLineHeight(), height + style.font.getLineHeight());
		
		label.setAlignment(Align.center, Align.center);
		label.setBounds((width + style.font.getLineHeight()) / 2 - width
				/ 2, (height + style.font.getLineHeight()) / 2 - height
				/ 2, width, height);
		label.setWrap(true);
		group.addActor(label);
	
		System.out.println(label.getWidth()+" cuoi cung"+label.getHeight());
		getContentTable().add(group).fill();
		return this;
	}
	
	@Override
	public MyDialog key(int keycode, Object object) {
		// TODO Auto-generated method stub
		super.key(keycode, object);
		return this;
	}
	
	@Override
	public MyDialog text(String text, LabelStyle labelStyle) {
		// TODO Auto-generated method stub
		super.text(text, labelStyle);
		return this;
	}
	
	@Override
	public MyDialog button(String text, Object object, TextButtonStyle buttonStyle) {
		// TODO Auto-generated method stub
		super.button(text, object, buttonStyle);
		return this;
	}

	
	/** {@link #pack() Packs} the dialog and adds it to the stage, centered. */
	public MyDialog show () {
		super.show(stage);
		return this;
	}
	
	public MyDialog setShow(Stage stage) {
		this.stage = stage;
		return this;
	}
}
