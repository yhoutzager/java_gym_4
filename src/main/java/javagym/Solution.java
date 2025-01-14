package javagym;

import noedit.*;
import org.apache.commons.lang3.Validate;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static javagym.Action.*;
import static noedit.Cell.Exit;
import static noedit.Cell.Wall;

public class Solution {

	@Nonnull
	@CheckReturnValue
	public Path solve(@Nonnull Maze maze, @Nonnull final Position initialPosition) {
		Validate.isTrue(Wall != maze.get(initialPosition),
				"Started inside a wall; this should never happen");
		PathBuilder path = new PathBuilder(initialPosition);
		List<Node>[] exits = new List[maze.duration];
		Node[][][] mazeNodes = new Node[maze.duration][maze.width][maze.height];


		System.out.println(maze.asStringAll());

		// 1. Exit vinden
		// TODO: 23-11-19 time factor (beginnen bij initial position time.)
		FOUND:
		for (int t = initialPosition.t; t < maze.duration; t++) {
			exits[t] = new ArrayList<>();
			for (int x = 0; x < maze.width; x++) {
				for (int y = 0; y < maze.height; y++) {
					Cell cell = maze.get(t, x, y);
					if (Exit.equals(cell)) {
						exits[t].add(new Node(x, y, false));
					}
					mazeNodes[t][x][y] = new Node(x, y, Wall.equals(maze.get(t, x, y)));
				}
			}
		}
//		if (exits.size() < 1) {
//			throw new IllegalStateException("No exits found after the starting position");
//		}
		// 2. Is er een exit op deze t? Probeer deze te bereiken.
		// A* proberen te maken
		// meerdere exits op zelfde niveau kan misschien in in het algoritme verwerkt worden.

		List<Stack<Action>> actions = new ArrayList<>();
		boolean found = false;
		for (int t = initialPosition.t; !found && t < maze.duration; t++) {
			if (exits[t].isEmpty()) {
				if (t < maze.duration - 1) {// TODO: 24-11-19 lelijk
					Stack stack = new Stack<Action>();
					stack.push(WAIT);
					actions.add(stack);
				}
				continue;
			}
			Stack<Action> newActions = findPath(mazeNodes[t], new Node(initialPosition.x, initialPosition.y, false), exits[t]);
			if (newActions != null) {
				actions.add(newActions);
				found = true;
				break;
				// TODO: 24-11-19 return path.build hier
			} else if (t < maze.duration - 1) { // TODO: 24-11-19 check ugly if
				Stack stack = new Stack<Action>();
				stack.push(WAIT);
				actions.add(stack);
			}
		}

		// 3. Als er bewogen moet worden tussen tijden.
		// Strategie?
		// weighted safe cells per tijdstip en daar over loopen?


		// TODO: 23-11-19 plan uitvoeren
		for (Stack<Action> actionStack : actions) {
			while (!actionStack.isEmpty()) {
				switch (actionStack.pop()) {
					case UP:
						path.up();
						break;
					case RIGHT:
						path.right();
						break;
					case DOWN:
						path.down();
						break;
					case LEFT:
						path.left();
						break;
					case WAIT:
						path.waitStep();
						break;
				}
			}
		}
		return path.build();
	}

	private Stack<Action> findPath(Node[][] maze, Node startingPosition, List<Node> endPositions) {
		// TODO: 23-11-19 endPositions een list maken en bij eCost bepalen welke dichtsbijzijnde is.
		startingPosition.eCost = determineClearStepDistance(startingPosition, endPositions);

		LinkedList<Node> openList = new LinkedList<>();
		openList.add(startingPosition);
		LinkedList<Node> closedList = new LinkedList<>();

		Node current;
		while (true) {
			current = lowestCostNode(openList);
			closedList.add(current);
			openList.remove(current);
//			System.out.println(current.x + " " + current.y + "    " + current.sCost + " " + current.eCost + " " + current.getFCosts());

			if (endPositions.contains(current)) {
				return makeActionStack(current);
				// TODO: 23-11-19 Goede return hier maken.
			}

			List<Node> adjacentNodes = determineAdjacentNodes(maze, current, closedList);
			for (Node currentAdjacentNode : adjacentNodes) {
				int index = openList.indexOf(currentAdjacentNode);
				if (index < 0) {
					currentAdjacentNode.previous = current;
					currentAdjacentNode.sCost = current.sCost + 1;
					currentAdjacentNode.eCost = determineClearStepDistance(currentAdjacentNode, endPositions);
					openList.add(currentAdjacentNode);
				} else {
					Node node = openList.get(index);
					if (node.sCost > current.sCost + 1) {
						node.previous = current;
						node.sCost = current.sCost + 1;
					}

				}
			}

			if (openList.isEmpty()) {
				return null;
			}

		}
	}

	private Stack<Action> makeActionStack(Node node) {
		Stack<Action> actionStack = new Stack<>();
		while (node.previous != null) {
			actionStack.push(determineAction(node.previous, node));
			node = node.previous;
		}
		return actionStack;
	}

	private Action determineAction(Node fromNode, Node toNode) {
		int diffX = fromNode.x - toNode.x;
		int diffY = fromNode.y - toNode.y;

		if (diffX == 0 & diffY < 0) {
			return DOWN;
		}
		if (diffX == 0 & diffY > 0) {
			return UP;
		}
		if (diffX < 0 & diffY == 0) {
			return RIGHT;
		}
		if (diffX > 0 & diffY == 0) {
			return LEFT;
		}
		throw new IllegalStateException();
	}

	private List<Node> determineAdjacentNodes(Node[][] maze, Node current, List<Node> closedList) {
		int x = current.x;
		int y = current.y;
		List<Node> adjacentNodes = new LinkedList<>();

		// Top
		if (y > 0 && !maze[x][y - 1].isWall) {
			Node temp = maze[x][y - 1];
			if (!closedList.contains(temp)) {
				adjacentNodes.add(temp);
			}
		}
		// Right
		if (x < maze.length - 1 && !maze[x + 1][y].isWall) {
			Node temp = maze[x + 1][y];
			if (!closedList.contains(temp)) {
				adjacentNodes.add(temp);
			}
		}
		// Buttom
		if (y < maze[0].length - 1 && !maze[x][y + 1].isWall) {
			Node temp = maze[x][y + 1];
			if (!closedList.contains(temp)) {
				adjacentNodes.add(temp);
			}
		}
		// Left
		if (x > 0 && !maze[x - 1][y].isWall) {
			Node temp = maze[x - 1][y];
			if (!closedList.contains(temp)) {
				adjacentNodes.add(temp);
			}
		}
		return adjacentNodes;
	}

	private Node lowestCostNode(List<Node> openList) {
		Node cheapest = openList.get(0);
		for (int i = 1; i < openList.size(); i++) {
			if (openList.get(i).getFCosts() > cheapest.getFCosts()) {
				continue;
			}
			if (openList.get(i).getFCosts() == cheapest.getFCosts()
					&& openList.get(i).eCost > cheapest.eCost) {
				continue;
			}
			cheapest = openList.get(i);
		}
		return cheapest;
	}

	private int determineClearStepDistance(Node pos1, List<Node> pos2) {
		int shortest = Math.abs(pos1.x - pos2.get(0).x) + Math.abs(pos1.y - pos2.get(0).y);
		for (int i = 1; i < pos2.size(); i++) {
			int temp = Math.abs(pos1.x - pos2.get(i).x) + Math.abs(pos1.y - pos2.get(i).y);
			if (temp < shortest) {
				shortest = temp;
			}
		}
		return shortest;
	}
}

class Node {
	final int x, y;
	int sCost, eCost;
	boolean isWall;
	Node previous;

	Node(int x, int y, boolean isWall) {
		this.x = x;
		this.y = y;
		this.isWall = isWall;
		sCost = 0;
	}

	int getFCosts() {
		return sCost + eCost;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Node) {
			return equals((Node) obj);
		}
		return super.equals(obj);
	}

	boolean equals(Node node) {
		return this.x == node.x && this.y == node.y;
	}
}

enum Action {
	UP,
	RIGHT,
	DOWN,
	LEFT,
	WAIT
}
