package oglib;

/* 
 * DRAW CIRCLE
 *En este programa se trazan los puntos perimetrales de un circulo dado las coordenadas centrales 
 *y el radio, se utiliza un algoritmo para calcular todos los puntos perimetrales del circulo en la 
 *primera parte del octante, después se imprimen los demás puntos especulares en diferentes octantes. 
 *El circulo es una figura simétrica, es por eso por lo que dicho algoritmo funciona. Existe una 
 *condición limite, teniendo la coordenada de cualquier píxel (x, y), se trazará el píxel (x, y+1) 
 *solo si se encuentra dentro o en el perímetro del circulo, caso contrario (x-1, y) si este se 
 *encuentra fuera del perímetro. 
 *Para calcular si dicho punto se encuentra dentro o fuera de la figura, utilizamos la fórmula: 
 *P = x2 + y2 – r2, si es negativo esta dentro, si es neutro es el perímetro y si es positivo se 
 *encuentra fuera del circulo. Para trazar el primer punto, es (r,0) en el eje x. 
 *
 * DRAW LINE
 * Al trazar una línea en cualquier plano 2D, necesitamos conectar dos puntos para obtener un segmento.
 * Entonces se calcula las coordenadas de un punto intermedio, para ello se toma la primera coordenada 
 * y va metiendo puntos dentro del segmento donde se encuentra la segunda coordenada. Se utilizaron las 
 * funciones gráficas, donde nuestra pantalla se convierte en un plano de coordenadas gráficas, donde la 
 * esquina inferior izquierda es la coordenada (0,0), a medida que vamos avanzando hacia la derecha va
 * incrementando en el eje de la X, y hacia arriba en el eje de la Y. Para generar puntos intermedios se 
 * utilizó el algoritmo DDA. 
 * 
*/

import java.io.IOException;

import oglib.components.CompileException;
import oglib.components.CreateException;
import oglib.components.Program;
import oglib.game.GameState;
import oglib.gui.Simple2DBuffer;
import oglib.gui.WindowGL;

import static org.lwjgl.opengl.GL20.*;

public class App {
    public static void main(String[] args) {
        var gameState = GameState.getGameState();
        var width = 500;
        var height = 500;
        var w = new WindowGL(width, height, "Figura Geometrica", gameState);

        try {
            var program = new Program("screen.vert", "screen.frag");
            var screen = new Simple2DBuffer(width, height);
            

            drawCircle(screen, 250, 250, 100);
    
            drawCircle(screen, 250, 250, 5);
            drawCircle(screen, 250, 250, 4);
            drawCircle(screen, 250, 250, 3);
            drawCircle(screen, 250, 250, 2);
            drawCircle(screen, 250, 250, 1);
            
            
            drawCircle(screen, 250, 350, 100);
            drawCircle(screen, 250, 150, 100);
            

            drawCircle(screen, 337, 302, 100);
            drawCircle(screen, 339, 200, 100);
            drawCircle(screen, 163, 300, 100);
            drawCircle(screen, 165, 198, 100);
            
            /*(donde inicia)(coordenada que deseas alcanzar)*/
            drawLine(screen, 50, 50, 112, 112);  /*  / (diag)   */
            drawLine(screen, 50, 450, 112, 388);  /*  \ (diag)   */
            drawLine(screen, 450, 450, 389, 389);  /*  / (diag)   */
            drawLine(screen, 450, 50, 389, 111);  /*  \ (diag)   */

            drawLine(screen, 50, 450, 450, 450); /*  - (arriba) */
            drawLine(screen, 50, 50, 50, 450);   /*  | (izq)    */ 
            drawLine(screen, 450, 50, 50, 50);   /* - (abajo)   */
            drawLine(screen, 450, 50, 450, 450); /*  | (der)    */

            
            while (!w.windowShouldClose()) {
                glClearColor(0f, 0f, 0f, 1.0f);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                program.use();
                screen.draw();
                w.swapBuffers();
                w.pollEvents();
            }
            w.destroy();
        } catch (IOException | CreateException | CompileException e) {
            e.printStackTrace();
        }

    }
    private static void drawLine(Simple2DBuffer screen, int X0, int Y0, int X1, int Y1) {
        
        float dx = X1 - X0;
        float dy = Y1 - Y0;

        float steps = valorAbs(dx) > valorAbs(dy) ? valorAbs(dx) : valorAbs(dy);

        float Xinic = dx / (float) steps;
        float Yinic = dy / (float) steps;

        float x = X0;
        float y = Y0;
        var z = 250;
        
        for(int i = 0; i <= steps; i++){
            screen.set(Math.round(x), Math.round(y), z, z, z);
            x += Xinic;
            y += Yinic;
        }

		
    }
    
    private static float valorAbs(float n) {
        return ( (n>0) ? n : ( n * (-1))); 
    }
    private static void drawCircle(Simple2DBuffer screen, final int X, final int Y, final int radius) {
		int x = 0;
        int y = radius;
        int d = (5 - radius * 4) / 4;

		do {
           
			screen.set(X + x, Y + y, 200, 200, 200);
			screen.set(X + x, Y - y, 200, 200, 200);
			screen.set(X - x, Y + y, 200, 200, 200);
            screen.set(X - x, Y - y, 200, 200, 200);
			screen.set(X + y, Y + x, 200, 200, 200);
			screen.set(X + y, Y - x, 200, 200, 200);
			screen.set(X - y, Y + x, 200, 200, 200);
			screen.set(X - y, Y - x, 200, 200, 200);
			if (d < 0) {                               //modificación en x, y, d
				d += 2 * x + 1;
			} else {
				d += 2 * (x - y) + 1;
				y--;
			}
			x++;                                       //se traza cada uno de los pixeles
        } 
        while (x <= y);
    }
}