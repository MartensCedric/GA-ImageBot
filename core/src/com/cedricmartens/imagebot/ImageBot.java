package com.cedricmartens.imagebot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class ImageBot extends ApplicationAdapter {
	SpriteBatch batch;
	AssetManager assetManager;
	private Image originalImage;
	private List<Image> pop;
	@Override
	public void create () {
		batch = new SpriteBatch();
		assetManager = new AssetManager();

		assetManager.load("original.png", Texture.class);
		assetManager.finishLoading();

		originalImage = new Image((Texture) assetManager.get("original.png"));
		pop = new ArrayList<>();

		for(int i = 0; i < 4; i++)
			pop.add(new Image(400, 400));

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(originalImage.getTexture(), 0, 0);
		batch.draw(pop.get(0).getTexture(), 400, 0, 200, 200);
		batch.draw(pop.get(1).getTexture(), 400, 200, 200, 200);
		batch.draw(pop.get(2).getTexture(), 600, 0, 200, 200);
		batch.draw(pop.get(3).getTexture(), 600, 200, 200, 200);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
