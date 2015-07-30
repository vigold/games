package vn.sunnet.qplay.diamondlink.modules;

import java.util.ArrayList;
import java.util.Random;

import vn.sunnet.qplay.diamondlink.gameobjects.Diamond;
import vn.sunnet.qplay.diamondlink.logiceffects.Effect;
import vn.sunnet.qplay.diamondlink.math.DoublyLinkedList;
import vn.sunnet.qplay.diamondlink.math.Iterator;
import vn.sunnet.qplay.diamondlink.math.Operator;
import vn.sunnet.qplay.diamondlink.phases.GemAI;
import vn.sunnet.qplay.diamondlink.screens.GameScreen;



import com.badlogic.gdx.Gdx;



public class DiamondAI {
	public static final int THREE_SET = 0;
	public static final int FOUR_SET = 1;
	public static final int FIVE_SET = 2;
	public static final int L_SET = 3;
	public static final int C_SET = 4;
	public static final int TWO_SET = 5;
	/*AI STEP*/
	public static final int FIND_SOLUTION = 0;
	public static final int TRACE_WAY = 1;
	public static final int IDLE = 2;
	
	
	
	private final int threeSet[][] = {
			{1, 1, 2, 1},
			{1, 1, -1, 1},
			{-1, 1, -2, 1},
			{0, 2, 0, 3},
			
			{1, 1, 1, 2},
			{1, 1, 1, -1},
			{1, -1, 1, -2},
			{2, 0, 3, 0},
			
			{1, -1, 2, -1},
			{1, -1, -1, -1},
			{-1, -1, -2, -1},
			{0, -2, 0, -3},
			
			{-1, 1, -1, 2},
			{-1, -1, -1, 1},
			{-1, -1, -1, -2},
			{-2, 0, -3, 0}
	};
	private final int threeResults[][] = {
			{0, 1},
			{0, 1},
			{0, 1},
			{0, 1},
			
			{1, 0},
			{1, 0},
			{1, 0},
			{1, 0},
			
			{0, -1},
			{0, -1},
			{0, -1},
			{0, -1},
			
			{-1, 0},
			{-1, 0},
			{-1, 0},
			{-1, 0},
	};
	
	private final int fourSet[][] = {
			{1, -1, 1, 1, 1, 2},
			{1, -2, 1, -1, 1, 1},
			{2, 1, 1, 1, -1, 1},
			{1, 1, -1, 1, -2, 1},
			
			{-1, -1, -1, 1, -1, 2},
			{-1, -2, -1, -1, -1, 1},
			{2, -1, 1, -1, -1, -1},
			{1, -1, -1, -1, -2, -1}
	};
	
	private final int fourResults[][] = {
			{1, 0},
			{1, 0},
			{0, 1},
			{0, 1},
			
			{-1, 0},
			{-1, 0},
			{0, -1},
			{0, -1}
	};
	
	private final int three_four_pairs[]= {
			2 , 2, 3, -1,
			0, 0, 1, -1,
			6, 6, 7, -1,
			4, 4, 5, -1
	};
	
	private final int fiveSet[][] = {
			{1, -2, 1, -1, 1, 1, 1, 2},
			{2, 1, 1, 1, -1, 1, -2, 1},
			{-1, -2, -1, -1, -1, 1, -1, 2},
			{2, -1, 1, -1, -1, -1, -2, -1}
	};
	
	private final int fiveResults[][] = {
			{1, 0},
			{0, 1},
			{-1, 0},
			{0, -1}
	};
	
	private final int four_five_pairs[] = {
			0, 0, 1, 1,
			2, 2, 3, 3
	};
	
	private final int LSet[][] = {
			{1, -2, 1, -1, 3, 0, 2, 0},
			{3, 0, 2, 0, 1, 1, 1, 2},
			{-1, 1, -2 , 1, 0, 2, 0, 3},
			{2, 1, 1, 1, 0, 2, 0, 3},
			
			{-1, -2, -1, -1, -2, 0, -3, 0},
			{-2, 0, -3, 0, -1, 1, -1, 2},
			{0, -3, 0, -2, 2, -1, 1, -1},
			{0, -3, 0, -2, -1, -1, -2, -1}
	};
	
	private final int LResults[][] = {
			{1, 0},
			{1, 0},
			{0, 1},
			{0, 1},
			
			{-1, 0},
			{-1, 0},
			{0, -1},
			{0, -1}
	};
	
	private final int three_L_pairs[] = {
			3, -1, 2, 3,
			1, -1, 0, 1,
			6, -1, 7, 6,
			5, -1, 4, 5
	};
	
	private int step = IDLE;
	
	private int resultType = -1;
	
	private int[][] grid = null;
	private int[][] flag = null;
	
//	private MissionDiamond screen;
	private Integer[] gridCells = null;
	private DoublyLinkedList[] singleColors = null;
	private DoublyLinkedList fiveColor = null;
	class Result{
		int cells[] = null;
		int type = -1;
		int set = -1;
		int num = -1;
	}
	
	class Swap{
		public static final int BEGIN = 0;
		public static final int MOVING = 1;
		public static final int END = 2;
		
		int step = 0;
		int source = -1;
		int destination = -1;
		boolean swap = true;
		
	}
	
	class SwapGroup{
		public ArrayList<Swap> swaps = new ArrayList<DiamondAI.Swap>();
		public SwapGroup() {
			// TODO Auto-generated constructor stub
			swaps.clear();
		}
		
		public void insert(int index, Swap swap) {
			swaps.add(index, swap);
		}
		
		public void add(Swap swap) {
			swaps.add(swap);
		}
		
		public boolean remove(Swap swap) {
			return swaps.remove(swap);
		} 
		
		public void remove(int index) {
			if (index < 0 || index > swaps.size() - 1) return;
			swaps.remove(index);
		}
		
		public void clear() {
			swaps.clear();
		}
		
		public Swap getSwap(int index) {
			if (index < 0 || index > swaps.size() - 1) return null;
			return swaps.get(index);
		}
		
		public boolean isEmpty() {
			return swaps.size() == 0;
		}
	}
	
	private ArrayList<SwapGroup> ways = new ArrayList<DiamondAI.SwapGroup>();
	
	private ArrayList<Result> results_2 = new ArrayList<DiamondAI.Result>();
	private ArrayList<Result> results_3 = new ArrayList<DiamondAI.Result>();
	private ArrayList<Result> results_4 = new ArrayList<DiamondAI.Result>();
	private ArrayList<Result> results_5 = new ArrayList<DiamondAI.Result>();
	private ArrayList<Result> results_L = new ArrayList<DiamondAI.Result>();
	private ArrayList<Result> results_C = new ArrayList<DiamondAI.Result>();
	private Result optimizedResult = null;
	private Random random = new Random();
	private float eclapsedTime = 0;
	private GameScreen screen;
	
	public DiamondAI(GameScreen screen) {
		// TODO Auto-generated constructor stub
		this.screen = screen;
		onCreated();
	}
	
	public void onCreated() {
		gridCells = new Integer[64];
		for (int i = 0 ; i < gridCells.length; i++) {
			gridCells[i] = new Integer(i);
		}
		singleColors = new DoublyLinkedList[screen.COLOR_NUM];
		step = IDLE;
		this.grid = screen.grid;
		this.flag = screen.inGridFlag;
		fiveColor = new DoublyLinkedList();
		for (int i = 0 ; i < screen.COLOR_NUM; i++)
			singleColors[i] = new DoublyLinkedList();
	}
	
	public void update(float delta) {
		Gdx.app.log("test", "step "+step);
		switch (step) {
		case FIND_SOLUTION:
			findSolution();
			findWays();
			break;
		case TRACE_WAY:
			traceWays();
			break;
		case IDLE:
			senseAI();
			break;
		}
	}
	
	private boolean contineTrace() {
		if (!existCell(optimizedResult.cells[0])) return false;
//		System.out.println("way"+ways.size());
		for (int i = 0 ; i < ways.size(); i++) {
			SwapGroup group = ways.get(i);
			if (group.swaps.size() > 0) {
				Swap swap = group.swaps.get(0);
				int cell = swap.source;
				if (!existCell(cell)) return false;
			}
		}
		return true;
	}
	
	public void traceWays() {
		if (ways.size() > 0) {
			SwapGroup group = ways.get(0);
			if (group.isEmpty()) ways.remove(0);
			else {
				
				Swap swap = group.getSwap(0);
				System.out.println(swap.source+" 1 "+swap.destination+" swap step = "+swap.step);
				System.out.println(flag[row(swap.source)][col(swap.source)]+" "+flag[row(swap.destination)][col(swap.destination)]);
				switch (swap.step) {
				case Swap.BEGIN:
					// khoi tao swap dua tren co (hieu ung dac biet khong duoc di chuyen)
					if (!contineTrace()) {
						ways.clear();
						step = IDLE;
						return;
					}
					
					if (screen.logic.isSpecialEffect()) return;
					
					if (swap.swap) {
						// beginSwap
						if (existCell(swap.source) && existCell(swap.destination)) {
							((GemAI) screen.gamePhase[GameScreen.DIAMOND_AI])
							
									.beginSwap(swap.source, swap.destination);
							swap.step = Swap.MOVING;	
						}
					} else {
						// begginChain
						((GemAI) screen.gamePhase[GameScreen.DIAMOND_AI])
						.beginChain(swap.source, swap.destination);
						swap.step = Swap.END;	
					}
					break;
				case Swap.MOVING:
					// nhan
					break;
				case Swap.END:
					group.remove(0);
					break;
				}
			}
		} else step = IDLE;
	}
	
	private void senseAI() {
		Gdx.app.log("test", "IDLE");
		DoublyLinkedList opList = singleColors[0];
		for (int i = 1 ; i < screen.COLOR_NUM; i++) {
			if (opList.length() < singleColors[i].length()) opList = singleColors[i];
		}
//		if (opList.length() > 3) 
		step = FIND_SOLUTION;
		eclapsedTime += Gdx.graphics.getDeltaTime();
		if (eclapsedTime > 1.6f) {
			eclapsedTime = 0;	
		}
	}
	
	private void findSolution() {
		optimizedResult = null;
		results_2.clear();
		results_3.clear();
		results_4.clear();
		results_5.clear();
		results_C.clear();
		results_L.clear();
		findCSETSolution();
		if (results_C.size() == 0) {
			find3SetSolution();
			findLSetSolution();
			if (results_L.size() == 0) {
				find4SetSolution();
				find5SetSolution();
			}
		}		
		
//		Gdx.app.log("test", "kich thuoc 3 "+results_3.size());
		if (results_C.size() > 0) {
			optimizedResult = results_C.get(0);
		} else if (results_L.size() > 0) {
			optimizedResult = results_L.get(random.nextInt(results_L.size()));
		} else if (results_5.size() > 0) {
			optimizedResult = results_5.get(random.nextInt(results_5.size()));
		} else if (results_4.size() > 0) {
			optimizedResult = results_4.get(random.nextInt(results_4.size()));
		} else if (results_3.size() > 0) {
			optimizedResult = results_3.get(random.nextInt(results_3.size()));
		} else {// khi khong tim duoc cap nao
			find2SetSolution();
			if (results_2.size() > 0)
				optimizedResult = results_2.get(0);
		}
		step = TRACE_WAY;
	}
	
	private void findWaysOf3Set() {
		Swap swap = new Swap();
		swap.source = optimizedResult.cells[0];
		int row = swap.source / 8;
		int col = swap.source % 8;
		row = row + threeResults[optimizedResult.set][0];
		col = col + threeResults[optimizedResult.set][1];
		swap.destination = row * 8 + col;
		SwapGroup group = new SwapGroup();
		group.add(swap);
		ways.add(group);
	}
	
	private void findWaysOf4Set() {
		Swap swap = new Swap();
		swap.source = optimizedResult.cells[0];
		int row = swap.source / 8;
		int col = swap.source % 8;
		row = row + fourResults[optimizedResult.set][0];
		col = col + fourResults[optimizedResult.set][1];
		swap.destination = row * 8 + col;
		SwapGroup group = new SwapGroup();
		group.add(swap);
		ways.add(group);
	}
	
	private void findWaysOf5Set() {
		Swap swap = new Swap();
		swap.source = optimizedResult.cells[0];
		int row = swap.source / 8;
		int col = swap.source % 8;
		row = row + fiveResults[optimizedResult.set][0];
		col = col + fiveResults[optimizedResult.set][1];
		swap.destination = row * 8 + col;
		SwapGroup group = new SwapGroup();
		group.add(swap);
		ways.add(group);
	}
	
	private void findWaysOfLSet() {
		Swap swap = new Swap();
		swap.source = optimizedResult.cells[0];
		int row = swap.source / 8;
		int col = swap.source % 8;
		row = row + LResults[optimizedResult.set][0];
		col = col + LResults[optimizedResult.set][1];
		swap.destination = row * 8 + col;
		SwapGroup group = new SwapGroup();
		group.add(swap);
		ways.add(group);
	}
	
	private void findWaysOfCSet() {
		Swap swap = new Swap();
		swap.source = optimizedResult.cells[0];
		swap.destination = optimizedResult.cells[1];
		swap.swap = false;
		SwapGroup group = new SwapGroup();
		group.add(swap);
		ways.add(group);
	}
	
	private void findDesinations() {
		int cell = optimizedResult.cells[0];
		int cell1 = optimizedResult.cells[1];
		int cell2 = optimizedResult.cells[2];
		int row = cell / 8;
		int col = cell % 8;
		// buoc 1
		int cellResult1 = -1;
		float min = Integer.MAX_VALUE;
		int row1 = row - 1;
		int col1 = col;
		if (inGrid(row1, col1))
			if (distance(cell(row1, col1), cell1) < min) {
				min = distance(cell(row1, col1), cell1) ;
				cellResult1 = cell(row1, col1);
			}
		row1 = row + 1;
		col1 = col;
		if (inGrid(row1, col1))
			if (distance(cell(row1, col1), cell1) < min) {
				min = distance(cell(row1, col1), cell1) ;
				cellResult1 = cell(row1, col1);
			}
		row1 = row;
		col1 = col - 1;
		if (inGrid(row1, col1))
			if (distance(cell(row1, col1), cell1) < min) {
				min = distance(cell(row1, col1), cell1) ;
				cellResult1 = cell(row1, col1);
			}
		row1 = row;
		col1 = col + 1;
		if (inGrid(row1, col1))
			if (distance(cell(row1, col1), cell1) < min) {
				min = distance(cell(row1, col1), cell1) ;
				cellResult1 = cell(row1, col1);
			}
		Swap swap = new Swap();
		SwapGroup group = new SwapGroup();
		swap.source = cell1;
		swap.destination = cellResult1;
		group.add(swap);
		ways.add(group);
		min = Integer.MAX_VALUE;
		int cellResult2 = -1;
		int row2 = 0;
		int col2 = 0;
		if (row(cell) ==  row(cellResult1)) {// cung hang
			row2 = row(cell);
			col2 = Math.min(col(cell), col(cellResult1)) - 1;
			if (inGrid(row2, col2)) {
				if (distance(cell(row2, col2), cell2) < min) {
					min = distance(cell(row2, col2), cell2) ;
					cellResult2 = cell(row2, col2);
				}
			}
			
			row2 = row(cell);
			col2 = Math.max(col(cell), col(cellResult1)) + 1;
			
			if (inGrid(row2, col2)) {
				if (distance(cell(row2, col2), cell2) < min) {
					min = distance(cell(row2, col2), cell2) ;
					cellResult2 = cell(row2, col2);
				}
			}
		} else {// cung cot
			row2 = Math.min(row(cell), row(cellResult1)) - 1;
			col2 = col(cell);
			if (inGrid(row2, col2)) {
				if (distance(cell(row2, col2), cell2) < min) {
					min = distance(cell(row2, col2), cell2) ;
					cellResult2 = cell(row2, col2);
				}
			}
			
			row2 = Math.max(row(cell), row(cellResult1)) + 1;
			col2 = cell % 8;
			
			if (inGrid(row2, col2)) {
				if (distance(cell(row2, col2), cell2) < min) {
					min = distance(cell(row2, col2), cell2) ;
					cellResult2 = cell(row2, col2);
				}
			}
		}
		swap = new Swap();
		swap.source = cell2;
		swap.destination = cellResult2;
		group = new SwapGroup();
		group.add(swap);
		ways.add(group);
	}
	
	private void createWays_1() {
		int cell = optimizedResult.cells[0];
		int cell1 = optimizedResult.cells[1];
		int cell2 = optimizedResult.cells[2];
		Swap swap = ways.get(0).swaps.get(0);
		SwapGroup group = null;
		int cellResult1 = swap.destination;
		swap = ways.get(1).swaps.get(0);
		int cellResult2 = swap.destination;
		// tranh cell2
		int row1 = Math.min(cell1 / 8, cellResult1 / 8);
		int col1 = Math.min(cell1 % 8, cellResult1 % 8);
		int row2 = Math.max(cell1 / 8, cellResult1 / 8);
		int col2 = Math.max(cell1 % 8, cellResult1 % 8);
		boolean aspect[] = new boolean[4];
		for (int i = 0 ; i < aspect.length; i++)
			aspect[i] = true;
		if (cell2 / 8 == row1 && col1 <= (cell2 % 8)  && (cell2 % 8) <= col2) aspect[0] = false;
		else if (cell2 / 8 == row2 && col1 <= (cell2 % 8)  && (cell2 % 8) <= col2) aspect[2] = false;
		else if (cell2 % 8 == col1 && row1 <= (cell2 / 8)  && (cell2 / 8) <= row2) aspect[3] = false;
		else if (cell2 % 8 == col2 && row1 <= (cell2 / 8)  && (cell2 / 8) <= row2) aspect[1] = false;
		if (cell1 == row1 * 8 + col1 || cell1 == row2 * 8 + col2) {// duong cheo chinh
			if (aspect[0] && aspect[1]) {
				// chieu tu duoi len
				SwapGroup newGroup = new SwapGroup();
				for (int j = col1 ; j < col2; j++) { // nhanh 0 ->
					swap = new Swap();
					swap.source = cell(row1, j);
					swap.destination = cell(row1, j + 1);
					newGroup.add(swap);
				}
				for (int i = row1; i < row2; i++) {// nhanh 1 
					swap = new Swap();
					swap.source = cell(i, col2);
					swap.destination = cell(i + 1, col2);
					newGroup.add(swap);
				}
				group = ways.get(0);
				group.clear();
				for (int i = 0; i < newGroup.swaps.size(); i++) {
					swap = newGroup.getSwap(i);
					if (cell1 != row1 * 8 + col1) {// huong tu tren xuong
						int temp = swap.source;
						swap.source = swap.destination;
						swap.destination = temp;
						group.insert(0, swap);
					} else group.add(swap); // huong tu duoi len
				}
			} else if (aspect[2] && aspect[3]) {
				// chieu tu duoi len
				SwapGroup newGroup = new SwapGroup();
				for (int i = row1; i < row2; i++) {// nhanh 3
					swap = new Swap();
					swap.source = cell(i, col1);
					swap.destination = cell(i + 1, col1);
					newGroup.add(swap);
				}
				for (int j = col1 ; j < col2; j++) {//  nhanh 2
					swap = new Swap();
					swap.source = cell(row2, j);
					swap.destination = cell(row2, j + 1);
					newGroup.add(swap);
				}
				group = ways.get(0);
				group.clear();
				for (int i = 0; i < newGroup.swaps.size(); i++) {
					swap = newGroup.getSwap(i);
					if (cell1 != row1 * 8 + col1) {// huong tu tren xuong
						int temp = swap.source;
						swap.source = swap.destination;
						swap.destination = temp;
						group.insert(0, swap);
					} else group.add(swap); // huong tu duoi len
				}
			}
		} else {
			if (aspect[0] && aspect[3]) {
				SwapGroup newGroup = new SwapGroup();
				for (int j = col2 ; j > col1; j--) { // nhanh 0 ->
					swap = new Swap();
					swap.source = cell(row1, j);
					swap.destination = cell(row1, j - 1);
					newGroup.add(swap);
				}
				for (int i = row1; i < row2; i++) {// nhanh 3 
					swap = new Swap();
					swap.source = cell(i, col1);
					swap.destination = cell(i + 1, col1);
					newGroup.add(swap);
				}
				group = ways.get(0);
				group.clear();
				for (int i = 0; i < newGroup.swaps.size(); i++) {
					swap = newGroup.getSwap(i);
					if (cell1 != cell(row1, col2)) {// huong tu tren xuong
						int temp = swap.source;
						swap.source = swap.destination;
						swap.destination = temp;
						group.insert(0, swap);
					} else group.add(swap); // huong tu duoi len
				}
			} else {
				SwapGroup newGroup = new SwapGroup();
				for (int i = row1; i < row2; i++) {// nhanh 1 
					swap = new Swap();
					swap.source = cell(i, col2);
					swap.destination = cell(i + 1, col2);
					newGroup.add(swap);
				}
				
				for (int j = col2 ; j > col1; j--) { // nhanh 2 ->
					swap = new Swap();
					swap.source = cell(row2, j);
					swap.destination = cell(row2, j - 1);
					newGroup.add(swap);
				}
				group = ways.get(0);
				group.clear();
				for (int i = 0; i < newGroup.swaps.size(); i++) {
					swap = newGroup.getSwap(i);
					if (cell1 != cell(row1, col2)) {// huong tu tren xuong
						int temp = swap.source;
						swap.source = swap.destination;
						swap.destination = temp;
						group.insert(0, swap);
					} else group.add(swap); // huong tu duoi len
				}
			}
		}
	}
	
	private void createWays_2() {
		int cell = optimizedResult.cells[0];
		int cell1 = optimizedResult.cells[1];
		int cell2 = optimizedResult.cells[2];
		Swap swap = null;
		SwapGroup group = null;
		swap = ways.get(1).swaps.get(0);
		int cellResult2 = swap.destination;
		// cell2 va cellResut2
		int row1 = Math.min(cell2 / 8, cellResult2 / 8);
		int col1 = Math.min(cell2 % 8, cellResult2 % 8);
		int row2 = Math.max(cell2 / 8, cellResult2 / 8);
		int col2 = Math.max(cell2 % 8, cellResult2 % 8);
		if (cell2 == row1 * 8 + col1 || cell2 == row2 * 8 + col2) {// duong cheo chinh
			SwapGroup newGroup = new SwapGroup();
			for (int j = col1 ; j < col2; j++) { // nhanh 0 ->
				swap = new Swap();
				swap.source = cell(row1, j);
				swap.destination = cell(row1, j + 1);
				newGroup.add(swap);
			}
			for (int i = row1; i < row2; i++) {// nhanh 1 
				swap = new Swap();
				swap.source = cell(i, col2);
				swap.destination = cell(i + 1, col2);
				newGroup.add(swap);
			}
			group = ways.get(1);
			group.clear();
			for (int i = 0; i < newGroup.swaps.size(); i++) {
				swap = newGroup.getSwap(i);
				if (cell2 != row1 * 8 + col1) {// huong tu tren xuong
					int temp = swap.source;
					swap.source = swap.destination;
					swap.destination = temp;
					group.insert(0, swap);
				} else group.add(swap); // huong tu duoi len
			}
		} else {
			SwapGroup newGroup = new SwapGroup();
			for (int j = col2 ; j > col1; j--) { // nhanh 0 ->
				swap = new Swap();
				swap.source = cell(row1, j);
				swap.destination = cell(row1, j - 1);
				newGroup.add(swap);
			}
			for (int i = row1; i < row2; i++) {// nhanh 3 
				swap = new Swap();
				swap.source = cell(i, col1);
				swap.destination = cell(i + 1, col1);
				newGroup.add(swap);
			}
			group = ways.get(1);
			group.clear();
			for (int i = 0; i < newGroup.swaps.size(); i++) {
				swap = newGroup.getSwap(i);
				if (cell2 != cell(row1, col2)) {// huong tu tren xuong
					int temp = swap.source;
					swap.source = swap.destination;
					swap.destination = temp;
					group.insert(0, swap);
				} else group.add(swap); // huong tu duoi len
			}
		}
	}
	
	private void createWays() {
		if (optimizedResult == null) return;
		createWays_1();
		createWays_2();
		for (int i = 0; i < ways.size(); i++) {
			for (int j = 0; j < ways.get(i).swaps.size(); j++) {
				Swap swap =  ways.get(i).swaps.get(j);
				System.out.println(swap.source+"->"+swap.destination);
			}
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++");
	}
	
	private void findWaysOf2Set() {
		findDesinations();
		createWays();
	}
	
	private void findWays() {
		if (optimizedResult == null) return;
		ways.clear();
		switch (optimizedResult.type) {
		case THREE_SET:
			findWaysOf3Set();
			break;
		case FOUR_SET:
			findWaysOf4Set();
			break;
		case FIVE_SET:
			findWaysOf5Set();
			break;
		case L_SET:
			findWaysOfLSet();
			break;
		case C_SET:
			findWaysOfCSet();
			break;
		case TWO_SET:
			findWaysOf2Set();
			break;
		}
	}
	
	private void find2SetSolution() {
		results_2.clear();
		DoublyLinkedList opList = singleColors[0];
		for (int i = 1 ; i < screen.COLOR_NUM; i++) {
			if (opList.length() < singleColors[i].length()) opList = singleColors[i];
		}
		
		Iterator i = opList.first();
		Iterator j = null;
		Iterator k = null;
        Iterator e = opList.end();
        
        int min = Integer.MAX_VALUE;
        int rCell1 = -1;
        int rCell2 = -1;
        int rCell3 = -1;
        
		
		for (; !i.equals(e); i.fwrd()) {
			int cell1 = (Integer) i.data();
			j = new Iterator(i.fwrdElement());
			if (existCell(cell1))
				for (; !j.equals(e); j.fwrd()) {
					int cell2 = (Integer) j.data();
					k = new Iterator(j.fwrdElement());
					if (existCell(cell2))
						for (; !k.equals(e); k.fwrd()) {
							int cell3 = (Integer) k.data();
							if (existCell(cell3)) {
								if (distance(cell1, cell2)
										+ distance(cell1, cell3) < min) {
									min = distance(cell1, cell2)
											+ distance(cell1, cell3);
									rCell1 = cell1;
									rCell2 = cell2;
									rCell3 = cell3;
								}

								if (distance(cell2, cell1)
										+ distance(cell2, cell3) < min) {
									min = distance(cell2, cell1)
											+ distance(cell2, cell3);
									rCell1 = cell2;
									rCell2 = cell1;
									rCell3 = cell3;
								}

								if (distance(cell3, cell1)
										+ distance(cell2, cell3) < min) {
									min = distance(cell3, cell1)
											+ distance(cell2, cell3);
									rCell1 = cell3;
									rCell2 = cell1;
									rCell3 = cell2;
								}
							}
						}
				}
		}
		
		if (rCell1 != -1 && rCell2 != -1 && rCell3 != -1) {
			Result result = new Result();
			result.cells = new int[3];
			result.cells[0] = rCell1;
			result.cells[1] = rCell2;
			result.cells[2] = rCell3;
			if (distance(result.cells[0], result.cells[1]) > distance(
					result.cells[0], result.cells[2])) {
				int temp = result.cells[1];
				result.cells[1] = result.cells[2];
				result.cells[2] = temp;
			}
			result.num = 3;
			result.set = -1;
			result.type = TWO_SET;
			results_2.add(result);
		}
	}
	
	private int distance(int cell1, int cell2) {
		return Math.abs(cell1 / 8 - cell2 / 8)
				+ Math.abs(cell1 % 8 - cell2 % 8);
	}
	
	private void findCSETSolution() {
		/*C_ET*/
		results_C.clear();
		Result opResult = null;
		Iterator i = fiveColor.first();
		Iterator e = fiveColor.end();
		for (; !i.equals(e); i.fwrd()) {
			int cell = (Integer) i.data();
			if (existCell(cell)) {
				if (!isSingleColor(cell)) {
					Result result = findCSETResult(cell);
					if (result != null)
						if (opResult == null)
							opResult = result;
						else if (opResult.num < result.num)
							opResult = result;
				}
			}
		}
		
//		for (int cell = 0; cell < 64; cell++)
//			if (existCell(cell)) {
//				if (!isSingleColor(cell)) {
//					Result result = findCSETResult(cell);
//					if (result != null)2
//						if (opResult == null)
//							opResult = result;
//						else if (opResult.num < result.num)
//							opResult = result;
//				}
//			}
		if (opResult != null) {
			if (opResult.num > 3) results_C.add(opResult);
		}
	}
	
	private void find3SetSolution() {
		/*3_SET*/
		results_3.clear();
		int cells[] = new int[2];
		for (int cell = 0; cell < 64; cell++)
			if (existCell(cell) && isSingleColor(cell)) {
//				Gdx.app.log("test", "cell0 = "+cell+"+++++++++++++++++++++"+threeSet.length+" "+threeSet[0].length);
				
				for (int set = 0 ; set < threeSet.length; set++) {
					int color = isColor(cell);
					cells[0] = -1; cells[1] = -1;
					for (int index = 0 ; index < threeSet[0].length / 2; index++) {
						int i = cell / 8;
						int j = cell % 8;
						int row = i + threeSet[set][index * 2];
						int col = j + threeSet[set][index * 2 + 1];
//						Gdx.app.log("test", "cell check"+(index + 1)+"= "+row+" "+col);
						if (inGrid(row, col)) 
							if (existCell(row, col) && isSingleColor(row, col)){
							int curColor = isColor(row, col);
							if (color == curColor) {
								cells[index] = row * 8 + col;
//								Gdx.app.log("test", "cell cung mau"+(index + 1)+"= "+cells[index]);
							}
						}	
					}
					if (cells[0] != -1 && cells[1] != -1) {
						Result result = new Result();
						result.cells = new int[3];
						result.cells[0] = cell;
						result.cells[1] = cells[0];
						result.cells[2] = cells[1];
						result.type = THREE_SET;
						result.num = 3;
						result.set = set;
						results_3.add(result);
					}
				}
			}
	}
	
	private void find4SetSolution() {
		/*4_SET*/
		results_4.clear();
		int[] cells = new int[3];
		for (int k = 0 ; k < results_3.size(); k++) {
			Result result = results_3.get(k);
			int cell = result.cells[0];
			int set = three_four_pairs[result.set];
			int color = isColor(cell);
			if (set > -1) {
				cells[0] = -1; cells[1] = -1; cells[2] = -1;
				for (int index = 0; index < fourSet[0].length / 2; index++) {
					int i = cell / 8;
					int j = cell % 8;
					int row = i + fourSet[set][index * 2];
					int col = j + fourSet[set][index * 2 + 1];
					if (inGrid(row, col)) 
						if (existCell(row, col) && isSingleColor(row, col)){
						int curColor = isColor(row, col);
						if (color == curColor) {
							cells[index] = row * 8 + col;
						}
					}	
				}
				if (cells[0] != -1 && cells[1] != -1 && cells[2] != -1) {
					result.cells = new int[4];
					result.cells[0] = cell;
					result.cells[1] = cells[0];
					result.cells[2] = cells[1];
					result.cells[3] = cells[2];
					result.type = FOUR_SET;
					result.num = 4;
					result.set = set;
					results_4.add(result);
					results_3.remove(k);
					k--;
				}
			}
		}
	}
	
	private void find5SetSolution() {
		results_5.clear();
		int[] cells = new int[4];
		for (int k = 0 ; k < results_4.size(); k++) {
			Result result = results_4.get(k);
			int cell = result.cells[0];
			int set = four_five_pairs[result.set];
			int color = isColor(cell);
			if (set > -1) {
				cells[0] = -1; cells[1] = -1; cells[2] = -1; cells[3] = -1;
				for (int index = 0; index < fiveSet[0].length / 2; index++) {
					int i = cell / 8;
					int j = cell % 8;
					int row = i + fiveSet[set][index * 2];
					int col = j + fiveSet[set][index * 2 + 1];
					if (inGrid(row, col)) 
						if (existCell(row, col) && isSingleColor(row, col)){
						int curColor = isColor(row, col);
						if (color == curColor) {
							cells[index] = row * 8 + col;
						}
					}	
				}
				if (cells[0] != -1 && cells[1] != -1 && cells[2] != -1 && cells[3] != -1) {
					result.cells = new int[5];
					result.cells[0] = cell;
					result.cells[1] = cells[0];
					result.cells[2] = cells[1];
					result.cells[3] = cells[2];
					result.cells[4] = cells[3];
					result.type = FIVE_SET;
					result.num = 5;
					result.set = set;
					results_5.add(result);
					results_4.remove(k);
					k--;
				}
			}
		}
	}
	
	private void findLSetSolution() {
		results_L.clear();
		int[] cells = new int[4];
		for (int k = 0 ; k < results_3.size(); k++) {
			Result result = results_3.get(k);
			int cell = result.cells[0];
			int set = three_L_pairs[result.set];
			int color = isColor(cell);
			if (set > -1) {
				cells[0] = -1; cells[1] = -1; cells[2] = -1; cells[3] = -1;
				for (int index = 0; index < LSet[0].length / 2; index++) {
					int i = cell / 8;
					int j = cell % 8;
					int row = i + LSet[set][index * 2];
					int col = j + LSet[set][index * 2 + 1];
					if (inGrid(row, col)) 
						if (existCell(row, col) && isSingleColor(row, col)){
						int curColor = isColor(row, col);
						if (color == curColor) {
							cells[index] = row * 8 + col;
						}
					}	
				}
				if (cells[0] != -1 && cells[1] != -1 && cells[2] != -1 && cells[3] != -1) {
					result.cells = new int[5];
					result.cells[0] = cell;
					result.cells[1] = cells[0];
					result.cells[2] = cells[1];
					result.cells[3] = cells[2];
					result.cells[4] = cells[3];
					result.type = L_SET;
					result.num = 5;
					result.set = set;
					results_L.add(result);
				}
			}
		}
	}
	
	private Result findCSETResult(int cell) {
		int i = cell / 8;
		int j = cell % 8;
		int row = -1;
		int col = -1;
		int num = 0;
		if (inGrid(i - 1, j)) 
			if (existCell(i - 1, j)) {
				int color = isColor(i - 1, j);
				int count = singleColors[color].length();
				if (num < count) {
					num = count;
					row = i - 1;
					col = j;
				}
			}
		if (inGrid(i, j - 1)) 
			if (existCell(i , j - 1)) {
				int color = isColor(i, j - 1);
				int count = singleColors[color].length();
				if (num < count) {
					num = count;
					row = i;
					col = j - 1;
				}
			}
		if (inGrid(i + 1, j)) 
			if (existCell(i + 1, j)) {
				int color = isColor(i + 1, j);
				int count = singleColors[color].length();
				if (num < count) {
					num = count;
					row = i + 1;
					col = j;
				}
			}
		if (inGrid(i, j + 1)) 
			if (existCell(i , j + 1)) {
				int color = isColor(i, j + 1);
				int count = singleColors[color].length();
				if (num < count) {
					num = count;
					row = i;
					col = j + 1;
				}
			}
		if (num > 0) {
			Result result = new Result();
			result.cells = new int[2];
			result.cells[0] = cell;
			result.cells[1] = row * 8 + col;
			result.type = C_SET;
			result.set = -1;
			result.num = num;
			return result;
		}
		return null;
	}
	
	public void finishSwap() {
		SwapGroup group = ways.get(0);
		Swap swap = group.swaps.get(0);
		if (swap.step == Swap.MOVING) swap.step = Swap.END;
	}
	
	public void removeCell(int row, int col) {
		if (grid[row][col] == -1) return;
		int type = isType(row, col);
		int color = isColor(row, col);
		if (type == Diamond.FIVE_COLOR_DIAMOND) {
			fiveColor.erase(gridCells[cell(row, col)]);
		} else {
			singleColors[color].erase(gridCells[cell(row, col)]);
		}
	}
	
	public void addCell(int row, int col) {
		if (grid[row][col] == -1) return;
		int type = isType(row, col);
		int color = isColor(row, col);
		if (type == Diamond.FIVE_COLOR_DIAMOND) {
			fiveColor.push_back(gridCells[cell(row, col)]);
		} else {
			singleColors[color].push_back(gridCells[cell(row, col)]);
		}
	}
	
	private boolean existCell(int cell) {
		return existCell(cell / 8, cell % 8);
	}
	
	private boolean existCell(int i, int j) {
		return (grid[i][j] != -1) && (certainCell(flag[i][j]));
	}
	
	public boolean certainCell(int value) {
		return (Operator.onBit(Effect.FIXED_POS, 1) == value);// || (Operator.getBit(Effect.FIXED_TO_FALL, value) > 0);
	}
	
	private boolean isSingleColor(int cell) {
		return isSingleColor(cell / 8, cell % 8);
	}
	
	private boolean isSingleColor(int i, int j) {
		return isType(i, j) != Diamond.FIVE_COLOR_DIAMOND;
	}
	
	private int isColor(int cell) {
		return isColor(cell / 8, cell % 8);
	}
	
	private int isColor(int i, int j) {
		return grid[i][j] % screen.COLOR_NUM;
	}
	
	private int isType(int cell) {
		return isType(cell / 8, cell % 8);
	}
	
	private int isType(int i, int j) {
		return grid[i][j] / screen.COLOR_NUM;
	}
	
	private int row(int cell) {
		return cell / 8;
	}
	
	private int col(int cell) {
		return cell % 8;
	}
	
	private int cell(int i, int j) {
		return i * 8 + j;
	}
	
	private boolean isNeighbourCell(int cell1 ,int cell2) {
		int row1 = cell1 / 8;
		int col1 = cell1 % 8;
		int row2 = cell2 / 8;
		int col2 = cell2 % 8;
		if (row1 != row2 && col1 != col2) return false;
		if (row1 == row2 && Math.abs(col1 - col2) == 1) return true;
		if (col1 == col2 && Math.abs(row1 - row2) == 1) return true;
		return false;
	}
	
	private boolean inGrid(int i, int j) {
		return !((i < 0) || (i > 7) || (j < 0) || (j > 7));
	}
}
