package drgMB;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
 * 
 * @author DMickDev
 *
 */
public abstract class HexTile {
	/**
	 * <p></p>
	 * @author DMickDev
	 *
	 */
	public enum AdjacentTile {
		UPPER_RIGHT (0, -1, 1),
		RIGHT (1, -1, 0),
		LOWER_RIGHT (2, -1, -1),
		LOWER_LEFT (3, 1, -1),
		LEFT (4, 1, 0),
		UPPER_LEFT (5, 1, 1);
		
		/**
		 * Refers to the position in the hex tile adjacent tiles array that
		 * corresponds to the adjacent tile in the position indicated by the
		 * enum type name
		 */
		private final int arrayPosition;
		/**
		 * Provides coordinates for the adjacent tile resolving method to use
		 * in identifying the correct tile
		 */
		private final int inverseXTarget;
		private final int inverseYTarget;
		
		/**
		 * <p>Creates a new preset. 
		 * @param arrayPosition the array index corresponding to this adjacency
		 * @param inverseXTarget the inverse of the x coordinate to search for
		 * to resolve this adjacency
		 * @param inverseYTarget the inverse of the y coordinate to search for
		 * to resolve this adjacency
		 */
		private AdjacentTile(int arrayPosition, int inverseXTarget, int inverseYTarget) {
			this.arrayPosition = arrayPosition;
			this.inverseXTarget = inverseXTarget;
			this.inverseYTarget = inverseYTarget;
		}
		
		/**
		 * @return the array index corresponding to this adjacency
		 */
		public int getArrayPosition() {
			return this.arrayPosition;
		}
		
		/**
		 * <p>Finds and returns the enum type that represents the link in the
		 * opposite direction to this one.</p>
		 * @return the opposite adjacency
		 */
		public AdjacentTile getOpposite() {
			return AdjacentTile.values()[(this.getArrayPosition() + 4) % 6];
		}

		/**
		 * @return the x coordinate of the linked tile relative to this adjacency
		 */
		public int getX() {
			return this.inverseXTarget;
		}
		
		/**
		 * @return the y coordinate of the linked tile relative to this adjacency
		 */
		public int getY() {
			return this.inverseYTarget;
		}
	}
	
	/**
	 * <p>A flag used to indicate whether the adjacency resolution method has
	 * discovered the tile at the correct coordinates.</p>
	 */
	private static boolean resolveFound = false;
	/**
	 * <p>A set of the tiles the adjacency resolution method has processed.</p>
	 */
	private static HashSet<UUID> visitedTiles;
	
	/**
	 * <p>The unique identifier of this tile.</p>
	 */
	private UUID tileUUID;
	/**
	 * <p>A collection of links to adjacent tiles. Often referred to below as
	 * "the link array."</p>
	 */
	private HexTile[] adjacentTiles;
	
	/**
	 * <p>Creates a new tile with a random UUID and no links to other tiles.</p>
	 */
	public HexTile() {
		this.tileUUID = UUID.randomUUID();
		this.adjacentTiles = new HexTile[6];
	}
	
	/**
	 * <p>Creates a new tile with a random UUID and populates its links with
	 * the provided map of <link direction, tile> construction.</p>
	 * @param adjacentTiles a map of adjacent tiles with included link directions
	 */
	public HexTile(HashMap<HexTile.AdjacentTile, HexTile> adjacentTiles) {
		this.tileUUID = UUID.randomUUID();
		this.adjacentTiles = new HexTile[6];
		for (AdjacentTile aT : adjacentTiles.keySet()) {
			this.adjacentTiles[aT.arrayPosition] = adjacentTiles.get(aT);
		}
	}
	
	/**
	 * <p>Returns an adjacent tile along the provided link direction.</p>
	 * @param position the relative position of the tile to get
	 * @return the tile at the that relative position
	 */
	public HexTile getAdjacentTile(HexTile.AdjacentTile position) {
		return this.adjacentTiles[position.arrayPosition];
	}
	
	/**
	 * <p>Returns an adjacent tile using the index of the underlying array.</p>
	 * @param position the index of the adjacent tile to retrieve
	 * @return the adjacent tile at the provided index
	 */
	public HexTile getAdjacentTile(int position) {
		if (position < 0 || position >= this.adjacentTiles.length) {
			throw new IllegalArgumentException();
		}
		
		return this.adjacentTiles[position];
	}
	
	/**
	 * <p>Returns the number of links this tile can have.</p>
	 * @return the size of the link array
	 */
	public int getNumAdjacentTiles() {
		return this.adjacentTiles.length;
	}
	
	/**
	 * @return the UUID of this tile
	 */
	public UUID getUUID() {
		return this.tileUUID;
	}
	
	/**
	 * <p>Uses recursion to find the tile at x == 0 and y == 0 without the need
	 * for a direct path to that tile.</p>
	 * @param tile the tile to process
	 * @param x the number of steps along the x axis to the desired tile
	 * @param y the number of steps along the y axis to the desired tile
	 * @return the tile at x == 0 and y == 0
	 */
	private HexTile resolveAdjacentTile(HexTile tile, int x, int y) {
		if (tile != null) {
			HexTile.visitedTiles.add(tile.tileUUID);
		}
		if (x == 0 & y == 0) {
			HexTile.resolveFound = true;
			return tile;
		}
		for (AdjacentTile aT : HexTile.AdjacentTile.values()) {
			// Explore each adjacent tile
			HexTile nextTile = tile.getAdjacentTile(aT.getArrayPosition());
			if (!HexTile.visitedTiles.contains(nextTile.getUUID())) {
				nextTile = this.resolveAdjacentTile(nextTile, x - aT.getX(), y - aT.getY());
				if (HexTile.resolveFound == true) {
					return nextTile;
				}
			}
		}
		return null;
	}
	
	/**
	 * <p>Discovers all tiles adjacent to this tile and links this tile to them.</p>
	 */
	public void resolveAdjacentTiles() {
		// Initialize the tile history set
		HexTile.visitedTiles.clear();
		// For each link direction for this tile
		for (AdjacentTile aT : HexTile.AdjacentTile.values()) {
			// Set the link to the tile discovered at the appropriate relative coordinates
			this.setAdjacentTile(this.resolveAdjacentTile(this, aT.getX(), aT.getY()), aT);
		}
		// Discard the tile history set
		HexTile.visitedTiles = null;
	}
	
	/**
	 * <p>Rotates the links of this tile, in effect, rotating the tile.</p>
	 * @param byAmount the number of positions to rotate the links by
	 */
	public void rotate(int byAmount) {
		// Reduce n to a number within the bounds of the link array
		byAmount %= this.adjacentTiles.length;
		// A new array to store the rotated link array
		HexTile[] rotatedArray = new HexTile[this.adjacentTiles.length];
		
		// For each link in the link array
		for (int i = 0; i < this.adjacentTiles.length; i++) {
			// Place the last n tiles in the old array at the beginning of the new array
			if (i < byAmount) {
				rotatedArray[i] = this.getAdjacentTile((this.adjacentTiles.length - byAmount) + i);
			}
			// Place the rest of the tiles after
			else {
				rotatedArray[i] = this.getAdjacentTile(i - byAmount);
			}
		}
		
		// Assign the new rotated array as the link array
		this.adjacentTiles = rotatedArray;
	}
	
	/**
	 * <p>Sets the provided tile in the adjacency provided.</p>
	 * @param tile the tile to link to this tile
	 * @param position the adjacency to fill
	 */
	public void setAdjacentTile(HexTile tile, HexTile.AdjacentTile position) {		
		this.adjacentTiles[position.arrayPosition] = tile;
	}
}
