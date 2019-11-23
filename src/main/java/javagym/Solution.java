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
		List<Position> exits = new ArrayList<>();
//		List<List<Node>> openCells = new ArrayList<>();


		System.out.println(maze.asStringAll());

		// 1. Exit vinden
		// TODO: 23-11-19 time factor (beginnen bij initial position time.)
		FOUND:
		for (int t = initialPosition.t; t < maze.duration; t++) {
//			openCells.add(new ArrayList<>());
			for (int x = 0; x < maze.width; x++) {
				for (int y = 0; y < maze.height; y++) {
					Cell cell = maze.get(t, x, y);
					if (Exit.equals(cell)) {
						exits.add(new Position(t, x, y));
					}
//					if (!Wall.equals(cell)) {
//						openCells.get(t).add(new Node(x, y));
//					}
				}
			}
		}
		if (exits.size() < 1) {
			throw new IllegalStateException("No exits found after the starting position");
		}
		// 2. Is er een exit op deze t? Probeer deze te bereiken.
		// A* proberen te maken
		// meerdere exits op zelfde niveau kan misschien in in het algoritme verwerkt worden.

		Node endPos = new Node(exits.get(0).x, exits.get(0).y);
		Stack<Action> actions = new Stack<>();
		if (maze.duration == 1) {
			 actions = findPath(maze.data[0], new Node(initialPosition.x, initialPosition.y), endPos);
		}

		// TODO: 23-11-19 plan uitvoeren
		while (!actions.isEmpty()) {
			switch (actions.pop()) {
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
			}
		}
		return path.build();
	}

	private Stack<Action> findPath(Cell[][] maze, Node startingPosition, Node endPosition) {
		// TODO: 23-11-19 endPositions een list maken en bij eCost bepalen welke dichtsbijzijnde is.
		startingPosition.eCost = determineClearStepDistance(startingPosition, endPosition);

		LinkedList<Node> openList = new LinkedList<>();
		openList.add(startingPosition);
		LinkedList<Node> closedList = new LinkedList<>();

		Node current;
		boolean found = false;
		while (!found) {
			current = lowestCostNode(openList);
			closedList.add(current);
			openList.remove(current);
//			System.out.println(current.x + " " + current.y + "    " + current.sCost + " " + current.eCost + " " + current.getFCosts());

			if (current.equals(endPosition)) {
				return makeActionStack(current);
				// TODO: 23-11-19 Goede return hier maken.
			}

			List<Node> adjacentNodes = determineAdjacentNodes(maze, current, closedList);
			for (Node currentAdjacentNode : adjacentNodes) {
				int index = openList.indexOf(currentAdjacentNode);
				if (index < 0) {
					currentAdjacentNode.previous = current;
					currentAdjacentNode.sCost = current.sCost + 1;
					currentAdjacentNode.eCost = determineClearStepDistance(currentAdjacentNode, endPosition);
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

		throw new IllegalStateException();
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

	private List<Node> determineAdjacentNodes(Cell[][] maze, Node current, List<Node> closedList) {
		int x = current.x;
		int y = current.y;
		List<Node> adjacentNodes = new LinkedList<>();

		// Top
		if (y > 0 && !Wall.equals(maze[x][y - 1])) {
			Node temp = new Node(x, y - 1);
			if (!closedList.contains(temp)) {
				adjacentNodes.add(temp);
			}
		}
		// Right
		if (x < maze.length - 1 && !Wall.equals(maze[x + 1][y])) {
			Node temp = new Node(x + 1, y);
			if (!closedList.contains(temp)) {
				adjacentNodes.add(temp);
			}
		}
		// Buttom
		if (y < maze[0].length - 1 && !Wall.equals(maze[x][y + 1])) {
			Node temp = new Node(x, y + 1);
			if (!closedList.contains(temp)) {
				adjacentNodes.add(temp);
			}
		}
		// Left
		if (x > 0 && !Wall.equals(maze[x - 1][y])) {
			Node temp = new Node(x - 1, y);
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

	private int determineClearStepDistance(Node pos1, Node pos2) {
		return Math.abs(pos1.x - pos2.x) + Math.abs(pos1.y - pos2.y);
	}
}

class Node {
	final int x, y;
	int sCost, eCost;
	Node previous;

	Node(int x, int y) {
		this.x = x;
		this.y = y;
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
	LEFT;
}
