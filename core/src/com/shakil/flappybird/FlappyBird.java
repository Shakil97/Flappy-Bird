package com.shakil.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.Random;


public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	//ShapeRenderer shapeRenderer;
	Texture[] birds;
	Texture gameOver;
	int flapState=0;
	float birdY=0;
	float velocity=0;
	int Score=0;
	int ScoringTube=0;
	BitmapFont font;
	Circle birdCircle;

	int gamestate=0;
	float gravity=2;
	Texture toptube;
	Texture bottomtube;
	float maxTubeOffset;
	float gap=400;
	Random randomGenerator;
	float tubeVelocity=5 ;
	int numberOfTube=4;
	float[] tubeX= new float[numberOfTube];
	float[] tubeOffset=new float[numberOfTube];
	float tubeDistance;
	Rectangle[] topTubeRectangle;
	Rectangle[] bottomTubeRectangle;
	
	@Override
	public void create () {
		batch=new SpriteBatch();
		background=new Texture("bg.png");
		birds=new Texture[2];
		birds[0]=new Texture("bird.png");
		birds[1]=new Texture("bird2.png");
		gameOver=new Texture("gameOver.png");
       // shapeRenderer=new ShapeRenderer();
        birdCircle = new Circle();

		toptube=new Texture("toptube.png");
		bottomtube=new Texture("bottomtube.png");
		randomGenerator=new Random();
		maxTubeOffset=Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		tubeDistance=Gdx.graphics.getWidth() *3 / 4;
		topTubeRectangle=new Rectangle[numberOfTube];
		bottomTubeRectangle=new Rectangle[numberOfTube];
		font=new BitmapFont();
		font.setColor(Color.WHITE);

		StartGame();




	}
	public void StartGame(){
		birdY=Gdx.graphics.getHeight() / 2 -birds[0].getHeight() / 2;
		for (int i=0; i<numberOfTube; i++){
			tubeOffset[i]=(randomGenerator.nextFloat() * 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i]= Gdx.graphics.getWidth() / 2 - toptube.getWidth()/2 + Gdx.graphics.getWidth()+ i * tubeDistance;

			topTubeRectangle[i]=new Rectangle();
			bottomTubeRectangle[i]=new Rectangle();
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

	    if (gamestate ==1) {

	    	if (tubeX[ScoringTube] < Gdx.graphics.getWidth() / 2) {

				Score++;
				if (ScoringTube < numberOfTube - 1){

					ScoringTube++;
				}else {
					ScoringTube=0;
				}
			}
        	if (Gdx.input.justTouched()){

				velocity =-25;

			}
			for (int i=0; i<numberOfTube; i++) {

				if (tubeX[i] <- toptube.getWidth() ){


					tubeX[i] +=numberOfTube*tubeDistance;
                    tubeOffset[i]=(randomGenerator.nextFloat() * 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				}else {

					tubeX[i] = tubeX[i] - tubeVelocity;
				}
				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);

				batch.draw(bottomtube, tubeX[i],
						Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i]);

				topTubeRectangle[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],toptube.getWidth(),toptube.getHeight());
				bottomTubeRectangle[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i],bottomtube.getWidth(),bottomtube.getHeight());


			}

            if (birdY >0 ) {

                velocity = velocity + gravity;
                birdY -= velocity;
            }else {
            	gamestate=2;
			}


     }else if(gamestate ==0) {
			if (Gdx.input.justTouched()){

				gamestate=1;
			}
		}
	    else if (gamestate==2){

	    	batch.draw(gameOver,Gdx.graphics.getWidth() / 2 - gameOver.getWidth()/2,Gdx.graphics.getHeight() / 2 - gameOver.getHeight()/2);

			if (Gdx.input.justTouched()){

				gamestate=1;
				StartGame();
				Score=0;
				ScoringTube=0;
				velocity=0;
			}

		}
		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}

		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);

		font.draw(batch,String.valueOf(Score),100,200);

        birdCircle.set(Gdx.graphics.getWidth() / 2,birdY + birds[flapState].getHeight() / 2,birds[flapState].getWidth() / 2);


//			shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//			shapeRenderer.setColor(Color.RED);
//			shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);


		for (int i=0; i<numberOfTube; i++) {

//			shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],toptube.getWidth(),toptube.getHeight());
//			shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i],bottomtube.getWidth(),bottomtube.getHeight());

			if (Intersector.overlaps(birdCircle,topTubeRectangle[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangle[i])){

				gamestate=2;

			}
		}
		batch.end();

		//shapeRenderer.end();

	}

	
	@Override
	public void dispose () {

	}
}
