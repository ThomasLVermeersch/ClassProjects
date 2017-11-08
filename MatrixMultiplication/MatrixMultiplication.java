public class MatrixMultiplication{
	public static void main(String[] args){
		int sum = 0;
		int[][] testm = new int[8][8];
		for(int i = 0; i < testm[0].length;i++){
			for(int j = 0; j < testm[0].length; j++){
				testm[i][j] = sum++;
			}
		}
		printMatrix(divideAndConquer(testm, testm));
		printMatrix(standardMultiplication(testm, testm));
	}

	public static int[][] standardMultiplication(int[][] m1, int[][] m2){
		int s = m1[0].length;
		int[][] m3 = new int[s][s];
		for(int i = 0; i < m1[0].length; i++){
			for(int j = 0; j < m1[0].length; j++){
				int sum = 0;
				for(int k = 0; k < m1[0].length; k++){
					m3[i][j] += m1[i][k] * m2[k][j];
				}
			}
		}
		return m3;
	}

	public static int[][] divideAndConquer(int[][] m1, int [][]m2){
		if(m1[0].length == 2){
			return standardMultiplication(m1,m2);
		} else {
			int[][] a11 = quarterUp(m1, 0);
			int[][] a12 = quarterUp(m1, 1);
			int[][] a21 = quarterUp(m1, 2);
			int[][] a22 = quarterUp(m1, 3);
			int[][] b11 = quarterUp(m2, 0);
			int[][] b12 = quarterUp(m2, 1);
			int[][] b21 = quarterUp(m2, 2);
			int[][] b22 = quarterUp(m2, 3);
			int[][] c11 = addMatrix(divideAndConquer(a11,b11), divideAndConquer(a12,b21));
			int[][] c12 = addMatrix(divideAndConquer(a11,b12), divideAndConquer(a12,b22));
			int[][] c21 = addMatrix(divideAndConquer(a21,b11), divideAndConquer(a22,b21));
			int[][] c22 = addMatrix(divideAndConquer(a21,b12), divideAndConquer(a22,b22));
		}
		return standardMultiplication(m1,m2);
	}

	public static int[][] strassensAlgorithm(int[][] m1, int[][] m2){
		
	}

	public static int[][] addMatrix(int[][] m1, int[][] m2){
		int s = m1[0].length;
		int[][] matrix = new int[s][s];
		for(int i = 0; i < s; i++){
			for(int j = 0; j < s; j++){
				matrix[i][j] = m1[i][j] + m2[i][j];
			}
		}
		return matrix;
	}

	public static void printMatrix(int[][] mx){
		for(int i = 0; i < mx[0].length; i++){
			for(int j = 0; j < mx[0].length; j++){
				System.out.print(mx[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static int[][] quarterUp(int[][] mx, int quadrant){
		int l = mx[0].length / 2;
		int[][] matrix = new int[l][l];
		switch(quadrant){
			case 0: 
				for(int i = 0; i < l; i++){
					for(int j = 0; j < l; j++){
						matrix[i][j] = mx[i][j];
					}
				}
				break;
			case 1: 
				for(int i = 0; i < l; i++){
					for(int j = 0; j < l; j++){
						matrix[i][j] = mx[i][j+l];
					}
				}
				break;
			case 2: 
				for(int i = 0; i < l; i++){
					for(int j = 0; j < l; j++){
						matrix[i][j] = mx[i+l][j];
					}
				}
				break;
			case 3: 
				for(int i = 0; i < l; i++){
					for(int j = 0; j < l; j++){
						matrix[i][j] = mx[i+l][j+l];
					}
				}
				break;
		}
		return matrix;
	}
}