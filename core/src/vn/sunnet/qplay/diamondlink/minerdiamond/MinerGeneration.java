package vn.sunnet.qplay.diamondlink.minerdiamond;

import java.util.ArrayList;
import java.util.Random;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;

import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.gameobjects.IDiamond;
import vn.sunnet.qplay.diamondlink.items.VipCard;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.modules.GenerationModule;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;


public class MinerGeneration extends GenerationModule{
	
	public boolean color[] = new boolean[7];
	
	public static int[][] UpGridBuff = new int[4][8];
	public static int[][] DownGridBuff = new int [2][8];
	
	static int[][] grid = new int[8][8];
	private static Random random = new Random();
	
	static int count = 0;
	static boolean result = false;
	
	public final int FIRE_POSIBILITY = 4;
	public final int COIN_POSIBILITY = 5;
	public final int CLOCK_POSIBILITY = 3;
	public int fireP = 4;
	public int coinP = 5;
	public int clockP = 3;
	public int countBomb = 0;
	
	public int[][] affectedCell = {
			{-1, -1, - 1, -2},
			{-2,  0,  -3,  0},
			{-1,  1,  -1,  2},
			
			{ 0, -2,   0, -3},
			{-1, -1,  -2, -1},
			
			{-1,  1,  -2,  1},
			
			{ 0, -2,  -1, -1},
			{-2,  0,  -1, -1},
			
			{-2,  0,  -1,  1},
			
			{-1,  -2,  0, -1},
			
			{-2,  -1, -1,  0},
			
			{-2,   1, -1,  0},
			
			{-1,   0, -3,  0},
			
			{ 0,  -3,  0, -1},
			
			{-1, -1, -1, 1}
	};
	
	public MinerGeneration(GameScreen screen) {
		super(screen);
		// TODO Auto-generated constructor stub
	}
	
	public  int[][] generateAll() {	
		Gdx.app.log("Ham sinh Quan","Sinh quan ban dau duoc goi");
		// Fomat gridBuffer to factory.
		for (int i = 0; i < 8; i++)
		for (int j = 0; j < 8; j++){
			gridBuffer[i][j] = -1;
		}
		
		// Khoi tao gridbuff phia tren
		for (int i = 0; i < 4; i ++)
		for (int j = 0; j < 8; j++)
			UpGridBuff[i][j] = random.nextInt(6);
		int solutions = 15;
		
		int numberSpecial = 0;
		if (is5Gems()) numberSpecial += 5;
		
		// Khoi tao kim cuong
		for (int i = 0 ; i < 8 ; i ++) 
			for (int j = 0 ; j < 8 ; j++) {
				for (int k = 0; k < 7; k++)
					color[k] = false;
				if (i > 1)
					if (diamondColor(gridBuffer[i - 1][j]) == diamondColor(gridBuffer[i - 2][j]))
						color[diamondColor(gridBuffer[i - 1][j])] = true;
				if (j > 1)
					if (diamondColor(gridBuffer[i][j - 1]) == diamondColor(gridBuffer[i][j - 2]))
						color[diamondColor(gridBuffer[i][j - 2])] = true;
				if (isDeletedWhite()) color[Diamond.WHITE] = true;
				
				int isChosen = MathUtils.random(0, 1);
				boolean hasSol = false;
				if (64 - (i * 8 + j) == solutions ) isChosen = 1;
				if (solutions == 0) isChosen = 0;
				if (isChosen == 1)
					for (int k = 0; k < affectedCell.length; k++) {
						int i1 = i + affectedCell[k][0];
						int j1 = j + affectedCell[k][1];
						int i2 = i + affectedCell[k][2];
						int j2 = j + affectedCell[k][3];
						if (i1 > 4 && i2 > 4 && i > 4)
							if (isInGrid(i1, j1) && isInGrid(i2, j2)) {
								if (diamondColor(gridBuffer[i1][j1]) == diamondColor(gridBuffer[i2][j2])
										&& !color[diamondColor(gridBuffer[i1][j1])]) {
									gridBuffer[i][j] = generateValue(diamondColor(gridBuffer[i1][j1]));// gridBuffer[i1][j1];
									solutions--;
									hasSol = true;
									break;
								}
							}
					}
				
				if (!hasSol) {
					int number = 5;
					for (int k = 0 ; k < 6 ; k++)
						if (color[k]) number--;
					int index = MathUtils.random(0, number);
					number = -1;
					for (int k = 0 ; k < 6 ; k++)
						if (!color[k]) {
							number++;
							if (number == index) {
								gridBuffer[i][j] = generateValue(k);
								hasSol = true;
								break;
							}
						}
				}
			}
		
		//Khoi tao long dat
		for (int i = 0 ; i < 4; i++)
		for (int j = 0; j < 8; j++){
			gridBuffer[i][j] = generateSoilDiamond(0);
		}
		
		gridBuffer[3][0] = random.nextInt(6);
		gridBuffer[3][7] = random.nextInt(6);
		
		// Khoi tao GridBuff phia duoi
		generateDownGridBuff(0);
		
		
		for (int i = 0 ; i < 8; i++)
			for (int j = 0; j < 8; j++){
				grid[i][j] = gridBuffer[i][j];
			}
		return grid;
	}
	
	public static int getScoreOfDiamond(int type, int color){ //6 7 8 
		int value = 0;
		switch (type) {
		case IDiamond.SOIL_DIAMOND:
			switch (color) {
			case IDiamond.BLUE:
			case IDiamond.GREEN:
			case IDiamond.ORANGE:
			case IDiamond.PINK:
				value = 0;
				break;
			case IDiamond.RED:
				value = 0;
				break;
			case IDiamond.WHITE:
				value = 0;
				break;
			case IDiamond.YELLOW:
				value = 0;
				break;
			}
			break;
		case IDiamond.MARK_DIAMOND:
			switch (color) {
			case IDiamond.BLUE:
				value = 500;
				break;
			case IDiamond.GREEN:
				value = 1000;
				break;
			case IDiamond.ORANGE:
				value = 2000;
				break;
			case IDiamond.PINK:
				value = 3000;
				break;
			case IDiamond.RED:
				value = 4000;
				break;
			case IDiamond.WHITE:
				value = 5000;
				break;
			case IDiamond.YELLOW:
				value = 0;
				break;
			}
			break;
		case IDiamond.LAVA:
		case IDiamond.BLUE_GEM:
		case IDiamond.DEEP_BLUE_GEM:
		case IDiamond.PINK_GEM:
		case IDiamond.RED_GEM:
			switch (color) {
			case IDiamond.BLUE:
				value = 0;
				break;
			case IDiamond.GREEN:
				value = 0;
				break;
			case IDiamond.ORANGE:
				value = 0;
				break;
			case IDiamond.PINK:
				value = 0;
				break;
			case IDiamond.RED:
				value = 0;
				break;
			case IDiamond.WHITE:
				value = 0;
				break;
			case IDiamond.YELLOW:
				value = 0;
				break;
			}
			break;
		}
		return value;
	}	
	
	public static int getCoinOfDiamond(int type, int color) {
		int value = 0;
		switch (type) {
		case IDiamond.SOIL_DIAMOND:
			switch (color) {
			case IDiamond.BLUE:
			case IDiamond.GREEN:
			case IDiamond.ORANGE:
			case IDiamond.PINK:
				value = 0;
				break;
			case IDiamond.RED:
				value = 50;
				break;
			case IDiamond.WHITE:
				value = 100;
				break;
			case IDiamond.YELLOW:
				value = 150;
				break;
			}
			break;
		case IDiamond.MARK_DIAMOND:
			switch (color) {
			case IDiamond.BLUE:
				value = 0;
				break;
			case IDiamond.GREEN:
				value = 0;
				break;
			case IDiamond.ORANGE:
				value = 0;
				break;
			case IDiamond.PINK:
				value = 0;
				break;
			case IDiamond.RED:
				value = 0;
				break;
			case IDiamond.WHITE:
				value = 0;
				break;
			case IDiamond.YELLOW:
				value = 0;
				break;
			}
			break;
		case IDiamond.LAVA:
			return 500;
		case IDiamond.BLUE_GEM:
			switch (color) {
			case IDiamond.BLUE:
				value = 250;
				break;
			case IDiamond.GREEN:
				value = 250;
				break;
			case IDiamond.ORANGE:
				value = 250;
				break;
			case IDiamond.PINK:
				value = 250;
				break;
			case IDiamond.RED:
				value = 0;
				break;
			case IDiamond.WHITE:
				value = 0;
				break;
			case IDiamond.YELLOW:
				value = 0;
				break;
			}
			break;
		case IDiamond.DEEP_BLUE_GEM:
			switch (color) {
			case IDiamond.BLUE:
				value = 300;
				break;
			case IDiamond.GREEN:
				value = 300;
				break;
			case IDiamond.ORANGE:
				value = 300;
				break;
			case IDiamond.PINK:
				value = 300;
				break;
			case IDiamond.RED:
				value = 0;
				break;
			case IDiamond.WHITE:
				value = 0;
				break;
			case IDiamond.YELLOW:
				value = 0;
				break;
			}
			break;
		case IDiamond.PINK_GEM:
			switch (color) {
			case IDiamond.BLUE:
				value = 350;
				break;
			case IDiamond.GREEN:
				value = 350;
				break;
			case IDiamond.ORANGE:
				value = 350;
				break;
			case IDiamond.PINK:
				value = 350;
				break;
			case IDiamond.RED:
				value = 0;
				break;
			case IDiamond.WHITE:
				value = 0;
				break;
			case IDiamond.YELLOW:
				value = 0;
				break;
			}
			break;
		case IDiamond.RED_GEM:
			switch (color) {
			case IDiamond.BLUE:
				value = 400;
				break;
			case IDiamond.GREEN:
				value = 400;
				break;
			case IDiamond.ORANGE:
				value = 400;
				break;
			case IDiamond.PINK:
				value = 400;
				break;
			case IDiamond.RED:
				value = 0;
				break;
			case IDiamond.WHITE:
				value = 0;
				break;
			case IDiamond.YELLOW:
				value = 0;
				break;
			}
			break;
		}
		return value;
	}
	
	public int[][] generatePart(int[] colHeight, int[][] gridFurture,
			int[][] gridFlag) {
		
		int solution = 6;
		MinerDiamond mScreen = (MinerDiamond) screen;
		
		solution -= mScreen.depth / 100;
		if (solution < 1) solution = 1;
		
		boolean hasSol = false;
		
		result = true;

		for (int i = 0; i < 8; i++)
		for (int j = 0; j < 8; j++){
			gridBuffer[i][j] = gridFurture[i][j] ;
		}
		
		for (int i = 0 ; i < 8 ; i++)
			for (int j = 0 ; j < 8 ; j++)
				gridBuffer[i][j] = -1;
		int[] pos = new int[8];
		for (int j = 0 ; j < 8 ; j++) {
			int num = -1;
			for (int i = 0 ; i < 8 ; i++)
				if (gridFurture[i][j] != -1 && certainCell(i, j)) {
					num++;
					gridBuffer[num][j] = gridFurture[i][j]; 
//					System.out.println("ton tai o duoi "+i+" "+j+"colHeight = "+colHeight[j]);
			}
			pos[j] = num + 1;		
		}
		
		for (int j = 0; j < 8 ; j++) {
			int i = pos[j] - 1;
			DiamondOfMiner pList = (DiamondOfMiner) screen.fall.inGridHead[j];
			while (pList != null) {
				i++;
				gridBuffer[i][j] = pList.getDiamondValue();
//				System.out.println("ton tai roi trong man "+i+" "+j);
				if (pList == screen.fall.inGridTail[j]) break;
				else pList = (DiamondOfMiner) pList.getNextDiamond();
			}
		}
		
		solution -= numSolution(gridBuffer);
			
		// Sinh quan theo thuat toan moi
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (gridBuffer[i][j] == -1) {
//					System.out.println("ton tai roi ngoai man "+i+" "+j);
					for (int k = 0; k < 7; k++)
						color[k] = false;
					if (i > 1)
						if (diamondColor(gridBuffer[i - 1][j]) == diamondColor(gridBuffer[i - 2][j]))
							color[diamondColor(gridBuffer[i - 1][j])] = true;
					if (j > 1)
						if (diamondColor(gridBuffer[i][j - 1]) == diamondColor(gridBuffer[i][j - 2]))
							color[diamondColor(gridBuffer[i][j - 2])] = true;
					hasSol = false;
					if (solution > 0) {
						for (int k = 0; k < affectedCell.length; k++) {
							int i1 = i + affectedCell[k][0];
							int j1 = j + affectedCell[k][1];
							int i2 = i + affectedCell[k][2];
							int j2 = j + affectedCell[k][3];

							if (isInGrid(i1, j1) && isInGrid(i2, j2)) {
								if (diamondColor(gridBuffer[i1][j1]) == diamondColor(gridBuffer[i2][j2])
										&& isDiamond(gridBuffer[i1][j1])
										&& isDiamond(gridBuffer[i2][j2])
										&& !color[diamondColor(gridBuffer[i1][j1])]) {
									gridBuffer[i][j] = generateValue(diamondColor(gridBuffer[i1][j1]));
									solution--;
									hasSol = true;
								}
							}
						}

						if (!hasSol) {
							int way = MathUtils.random(1, 2);
							if (way == 1 && i == 0) {
								if (j > 0)
									way = 2;
								else
									way = 3;
							}
							if (way == 2 && j == 0) {
								if (i > 0)
									way = 1;
								else
									way = 3;
							}

							switch (way) {
							// Create follow Row
							case 1:
								if (i > 0) {
									if (i > 1) {
										if (diamondColor(gridBuffer[i - 2][j]) != diamondColor(gridBuffer[i - 1][j]))
											gridBuffer[i][j] = generateValue(diamondColor(gridBuffer[i - 1][j]));
										else
											gridBuffer[i][j] = (byte) generateFromUpBuff(j);
									} else {
										gridBuffer[i][j] = (byte) generateFromUpBuff(j);
									}
								} else {
									gridBuffer[i][j] = (byte) generateFromUpBuff(j);
								}
								break;

							// Create follow col
							case 2:
								if (j > 0) {
									if (j > 1) {
										if (diamondColor(gridBuffer[i][j - 2]) != diamondColor(gridBuffer[i][j - 1]))
											gridBuffer[i][j] = generateValue(diamondColor(gridBuffer[i][j - 1]));
										else
											gridBuffer[i][j] = (byte) generateFromUpBuff(j);
									} else {
										gridBuffer[i][j] = (byte) generateFromUpBuff(j);
									}
								} else {
									gridBuffer[i][j] = (byte) generateFromUpBuff(j);
								}
								break;

							// Position Random
							case 3:
								gridBuffer[i][j] = (byte) generateFromUpBuff(j);
								break;
							}
						}
					} else {
						gridBuffer[i][j] = (byte) generateFromUpBuff(j);
					}
				}
			}
		if (!result)
			count++;
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				int type = diamondType(gridBuffer[i][j]);
				int color = diamondColor(gridBuffer[i][j]);
				if (color == Diamond.WHITE)
					if (isDeletedWhite()) 
						gridBuffer[i][j] = screen.logic.getDiamondValue(type, 0, 0);
			}

		return gridBuffer;
	}
	
	private int generateFromUpBuff(int col){
		int temp = random.nextInt(7);	
//		UpGridBuff[0][col] = UpGridBuff[1][col];
//		UpGridBuff[1][col] = UpGridBuff[2][col];
//		UpGridBuff[2][col] = UpGridBuff[3][col];
//		UpGridBuff[3][col] = random.nextInt(6);
		return generateValue(temp);
	}

	public int diamondType(int i) {
		i = i % (screen.COLOR_NUM * screen.TYPE_NUM);
		return i / screen.COLOR_NUM;
	}
	
	public int diamondColor(int i) {	
		i = i % (screen.COLOR_NUM * screen.TYPE_NUM);
		return i % screen.COLOR_NUM;
	}
	
	public void generateDownGridBuff(int depthMeter){
		for (int i = 0 ; i < 2 ; i++)
		for (int j = 0 ; j < 8 ; j++){
			DownGridBuff[i][j]  = generateSoilDiamond(depthMeter);
		}
	}
	
	public int generateSoilDiamond(int depthMeter){
		int n;
		n = random.nextInt(100) + 1;
		
		if (depthMeter >= 0 && depthMeter < 50){
			if (n <= 50){
//				return IDiamond.PINK_GEM * screen.COLOR_NUM + 2;
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM;
			}else if (n < 75){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 1;
			}else if (n <= 100){
//				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM;
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 4;
			}
		};
		
		if (depthMeter > 50 && depthMeter <= 100){
			if ( n <= 38){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM;
			}else if (n <= 76 ){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 1;
			}else if (n <= 81 ){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 4;
			}else if (n <= 98){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 5;
			}else{
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM;
			}	
		};
		
		if (depthMeter > 100 && depthMeter <= 150){
			if ( n <= 33){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM;
			}else if (n <= 53){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 1;
			}else if (n <= 63){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 2;
			}else if (n <= 73){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 4;
			}else if ( n <= 86){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 5;
			}else if (n <= 97){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 6;
			}else {
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM;
			}
		};
		
		if (depthMeter > 150 && depthMeter <= 200){
			if ( n <= 26){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM;
			}else if (n <= 36){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 1;
			}else if (n <= 51){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 2;
			}else if (n <= 56){
				return IDiamond.BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 61){
				return IDiamond.DEEP_BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 66){
				return IDiamond.PINK_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 71){
				return IDiamond.RED_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 84){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 4;
			}else if (n <= 87){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 5;
			}else if (n <= 90){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 6;
			}else if (n <= 95){
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM;
			}else{
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 1;
			}
		};
		
		if (depthMeter > 200 && depthMeter <= 250){
			if ( n <= 17){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM;
			}else if ( n <= 37){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 1;
			}else if (n <= 48){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 2;
			}else if (n <= 53){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 3;
			}else if (n <= 58){
				return IDiamond.BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 63){
				return IDiamond.DEEP_BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 68){
				return IDiamond.PINK_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 73){
				return IDiamond.RED_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 86){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 4;
			}else if (n <= 94){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 5;
			}else if (n <= 97){
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM;
			}else {
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 1;
			}			
		};
		
		if (depthMeter > 250 && depthMeter <= 300){
			if ( n <= 4){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM;
			}else if ( n <= 29){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 1;
			}else if (n <= 39){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 2;
			}else if (n <= 44){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 3;
			}else if (n <= 49){
				return IDiamond.BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 54){
				return IDiamond.DEEP_BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 59){
				return IDiamond.PINK_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 64){
				return IDiamond.RED_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 69){
				return IDiamond.LAVA * screen.COLOR_NUM;
			}else if (n <= 89){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 4;
			}
//			else if (n <= 94){
//				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 5;
//			}
			else if (n <= 94){
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM;
			}else if (n <= 97) {
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 1;
			}
			else{
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 2;
			}
		};
		
		if (depthMeter > 300 && depthMeter <= 350){
			if ( n <= 24){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 1;
			}else if (n <= 49){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 2;
			}else if (n <= 54){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 3;
			}else if (n <= 59){
				return IDiamond.BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 64){
				return IDiamond.DEEP_BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 69){
				return IDiamond.PINK_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 74){
				return IDiamond.RED_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 82){
				return IDiamond.LAVA * screen.COLOR_NUM;
			}else if (n <= 92){
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 1;
			}else if ( n <= 97){
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 2;
			}else{
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 3;
			}
		};
		
		if (depthMeter > 350 && depthMeter <= 400){
			if ( n <= 8){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 1;
			}else if (n <= 33){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 2;
			}else if (n <= 43){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 3;
			}else if (n <= 51){
				return IDiamond.BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if ( n <= 61){
				return IDiamond.DEEP_BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if ( n <= 71){
				return IDiamond.PINK_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 81){
				return IDiamond.RED_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 91){
				return IDiamond.LAVA * screen.COLOR_NUM;
			}else if (n <= 94){
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 1;
			}else if ( n <= 97){
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 2;
			}else{
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 3;
			}
	
		};
		
		if (depthMeter > 400 && depthMeter <= 500){
			if ( n <= 11){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 2;
			}else if (n <= 21){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 3;
			}else if (n <= 36){
				return IDiamond.BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 51){
				return IDiamond.DEEP_BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if ( n <= 66){
				return IDiamond.PINK_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if ( n <= 81){
				return IDiamond.RED_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 91){
				return IDiamond.LAVA * screen.COLOR_NUM;
			}else if (n <= 94){
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 2;
			}else if (n <= 97){
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 3;
			}else{
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 4;
			}											
		};
		
		if (depthMeter > 500 && depthMeter <= 550){
			if ( n <= 15){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 3;
			}else if (n <= 30){
				return IDiamond.BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 45){
				return IDiamond.DEEP_BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 60){
				return IDiamond.PINK_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if ( n <= 75){
				return IDiamond.RED_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if ( n <= 91){
				return IDiamond.LAVA * screen.COLOR_NUM;
			}else if (n <= 94){
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 2;	
			}else if (n <= 97){
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 3;
			}else{
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 4;
			}
		};
		
		if (depthMeter > 550 && depthMeter <= 600){
			if ( n <= 6){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 3;
			}else if (n <= 24){
				return IDiamond.BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 42){
				return IDiamond.DEEP_BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 60){
				return IDiamond.PINK_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if ( n <= 78){
				return IDiamond.RED_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if ( n <= 91){
				return IDiamond.LAVA * screen.COLOR_NUM;
			}else if (n <= 94){
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 3;
			}else if (n <= 97){
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 4;
			}else{
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 5;
			}													
		};
		
		if (depthMeter > 600 && depthMeter <= 650){
			if ( n <= 20){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 3;
			}else if (n <= 33){
				return IDiamond.BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 46){
				return IDiamond.DEEP_BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 59){
				return IDiamond.PINK_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if ( n <= 72){
				return IDiamond.RED_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if ( n <= 91){
				return IDiamond.LAVA * screen.COLOR_NUM;
			}else if (n <= 94){
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 3;
			}else if (n <= 97){
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 4;
			}else{
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 5;
			}				
		};
		
		if (depthMeter > 650){
			if ( n <= 15){
				return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM + 3;
			}else if (n <= 29){
				return IDiamond.BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 43){
				return IDiamond.DEEP_BLUE_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if (n <= 57){
				return IDiamond.PINK_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if ( n <= 71){
				return IDiamond.RED_GEM * screen.COLOR_NUM + MathUtils.random(2, 2);
			}else if ( n <= 95){
				return IDiamond.LAVA * screen.COLOR_NUM;
			}else {
				return IDiamond.MARK_DIAMOND * screen.COLOR_NUM + 5;
			}		
		};
		return IDiamond.SOIL_DIAMOND * screen.COLOR_NUM;
	}		

	public boolean isInGrid(int i, int j) {
		return (i > -1) && (i < 8) && (j > -1) && (j < 8);
	}
	
	public void swap(int x1, int y1, int x2, int y2) {
		if (isInGrid(x1, y1) && isInGrid(x2, y2)){
			int temp = gridBuffer[x1][y1];
			gridBuffer[x1][y1] = gridBuffer[x2][y2]; 
			gridBuffer[x2][y2] = temp;
		}		
	}
	
	public boolean isNoMove(int[][] grid){
		for (int i = 0; i < 8; i++)
		for (int j = 0; j < 8; j++){
			if (diamondType(grid[i][j]) == DiamondOfMiner.FIVE_COLOR_DIAMOND) return false;
			for (int k = 0 ; k < affectedCell.length / 4; k++) {
				int i1 = i + affectedCell[k][0];
				int j1 = j + affectedCell[k][1];
				int i2 = i + affectedCell[k][2];
				int j2 = j + affectedCell[k][3];
				if (isInGrid(i1, j1) && isInGrid(i2, j2)){
					if (diamondColor(grid[i1][j1]) == diamondColor(grid[i2][j2]) 
						&& diamondColor(grid[i1][j1]) == diamondColor(grid[i][j])
						&& isDiamond(grid[i1][j1]) 
						&& isDiamond(grid[i2][j2])
						&& isDiamond(grid[i][j])){
							return false;
					}
				}
			}
		}
	return true;	
	}

	public int numSolution(int[][] grid){
		int count = 0;
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++){
				if (diamondType(grid[i][j]) == DiamondOfMiner.FIVE_COLOR_DIAMOND) count++;
				for (int k = 0 ; k < affectedCell.length / 4; k++) {
					int i1 = i + affectedCell[k][0];
					int j1 = j + affectedCell[k][1];
					int i2 = i + affectedCell[k][2];
					int j2 = j + affectedCell[k][3];
					if (grid[i][j] >= 0)
					if (isInGrid(i1, j1) && isInGrid(i2, j2)){
						if (diamondColor(grid[i1][j1]) == diamondColor(grid[i2][j2]) 
							&& diamondColor(grid[i1][j1]) == diamondColor(grid[i][j])
							&& isDiamond(grid[i1][j1]) 
							&& isDiamond(grid[i2][j2])
							&& isDiamond(grid[i][j])){
								count++;
						}
					}
				}
			}
		return count;
	}
	
	public int[] RefreshMap(int[][] grid){
		int[] temp = new  int[8];
		int count = 0;
		for (int j = 0; j < 8; j++){
			count = 0;
			for (int i = 0; i < 8; i++)
			if (isDiamond(grid[i][j])){
				grid[i][j] = MathUtils.random(0 , 6);
				count++;
			}
			temp[j] = 8 -  count;
		}	
		if (numSolution(grid) == 0) RefreshMap(grid);
		return temp;
	}
	
	boolean isDiamond(int i){
		int type = diamondType(i);
		return (type == DiamondOfMiner.NORMAL_DIAMOND
				|| type == DiamondOfMiner.FIRE_DIAMOND
				|| type == DiamondOfMiner.BLINK_DIAMOND
				|| type == DiamondOfMiner.RT_DIAMOND || type == DiamondOfMiner.CT_DIAMOND);
	}
	
	private boolean certainCell(int row, int col) {
		return (Operator.hasOnly(Effect.FIXED_POS, screen.inGridFlag[row][col])); // 0);
	}

	@Override
	public int[][] genertateAll(int[][] grid) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				if (diamondType(screen.grid[i][j]) != IDiamond.NORMAL_DIAMOND) 
					gridBuffer[i][j] = grid[i][j];
				else gridBuffer[i][j] = -1;
			}
		int solutions = 10;
		
		for (int i = 0 ; i < 8 ; i ++) 
			for (int j = 0 ; j < 8 ; j++) {
				if (gridBuffer[i][j] != -1) continue;
				for (int k = 0 ; k < 7 ; k++)
					color[k] = false;
				if (i > 1) 
					if (isDiamond(gridBuffer[i - 1][j])
							&& isDiamond(gridBuffer[i - 2][j]))
						if (diamondColor(gridBuffer[i - 1][j]) == diamondColor(gridBuffer[i - 2][j]))
							color[diamondColor(gridBuffer[i - 1][j])] = true;
				if (j > 1)
					if (isDiamond(gridBuffer[i][j - 1])
							&& isDiamond(gridBuffer[i][j - 2]))
						if (diamondColor(gridBuffer[i][j - 1]) == diamondColor(gridBuffer[i][j - 2]))
							color[diamondColor(gridBuffer[i][j - 2])] = true;
				if (isDeletedWhite()) {
					color[Diamond.WHITE] = true;
				}
				int isChosen = MathUtils.random(0, 1);
				boolean hasSol = false;
				if (64 - (i * 8 + j) == solutions ) isChosen = 1;
				if (solutions == 0) isChosen = 0;
				if (isChosen == 1)
					for (int k = 0; k < affectedCell.length; k++) {
						int i1 = i + affectedCell[k][0];
						int j1 = j + affectedCell[k][1];
						int i2 = i + affectedCell[k][2];
						int j2 = j + affectedCell[k][3];
						if (isInGrid(i1, j1) && isInGrid(i2, j2)) {
							if (isDiamond(gridBuffer[i1][j1])
									&& isDiamond(gridBuffer[i2][j2]))
								if (diamondColor(gridBuffer[i1][j1]) == diamondColor(gridBuffer[i2][j1])
										&& !color[diamondColor(gridBuffer[i1][j1])]) {
									gridBuffer[i][j] = screen.logic
											.getDiamondValue(
													IDiamond.NORMAL_DIAMOND,
													diamondColor(gridBuffer[i1][j1]),
													0);

									solutions--;
									hasSol = true;
									break;
								}
						}
				}
				if (!hasSol) {
					int number = 6;
					for (int k = 0 ; k < 7 ; k++)
						if (color[k]) number--;
					int index = MathUtils.random(0, number);
					number = -1;
					for (int k = 0 ; k < 7 ; k++)
						if (!color[k]) {
							number++;
							if (number == index) {
								gridBuffer[i][j] = screen.logic
										.getDiamondValue(
												IDiamond.NORMAL_DIAMOND, k, 0);
								//Log.d("Generate", "ko duoc chinh "+i+" "+j+" "+grid[i][j]);
								hasSol = true;
								break;
							}
						}
				}
				if (!hasSol) Gdx.app.log("Generate", "loi o "+i+" "+j);
			}
//		gridBuffer[0][0] = Diamond.FIRE_DIAMOND * screen.COLOR_NUM;
//		gridBuffer[0][1] = Diamond.FIRE_DIAMOND * screen.COLOR_NUM;
		return gridBuffer;
	}
	
	private int generateValue(int color) {
		int p = random.nextInt(100);
		fireP = FIRE_POSIBILITY;
		coinP = COIN_POSIBILITY;
		clockP = CLOCK_POSIBILITY;
		if (isDoubleFire()) fireP = 2 * fireP;
		
		if (p < fireP) {
			return screen.logic.getDiamondValue(IDiamond.FIRE_DIAMOND, color, 0);
		} else if (p < fireP + coinP) {
			return screen.logic.getDiamondValue(IDiamond.NORMAL_DIAMOND, color, 0);
		} else if (p < fireP + coinP + clockP) {
			return screen.logic.getDiamondValue(IDiamond.NORMAL_DIAMOND, color, 0);
		}
		return screen.logic.getDiamondValue(IDiamond.NORMAL_DIAMOND, color, 0);
	}
}
