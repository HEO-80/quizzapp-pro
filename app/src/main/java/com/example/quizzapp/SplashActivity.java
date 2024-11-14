package com.example.quizzapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase SplashActivity que hereda de AppCompatActivity.
 * Muestra una pantalla de bienvenida y carga las preguntas del cuestionario.
 *
 */

//Derivar esta clase a base de datos SQL
public class SplashActivity extends AppCompatActivity {
    public static ArrayList<ModelClass> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Aquí se puede agregar cualquier inicialización adicional que sea necesaria
        // para la lista de preguntas o cualquier otra cosa relacionada.
    }

    @Override
    protected void onResume() {
        super.onResume();


        // Obtener la categoría seleccionada del Intent
        String selectedCategoria = getIntent().getStringExtra("categoria");

        // Obtener la lista completa de preguntas
        ArrayList<ModelClass> allQuestionsList = getList();

        // Filtrar las preguntas según la categoría seleccionada
        List<ModelClass> filteredQuestionsList = new ArrayList<>();
        for (ModelClass question : allQuestionsList) {
            for (String categoria : question.categorias) {
                if (categoria.equals(selectedCategoria)) {
                    filteredQuestionsList.add(question);
                    break;
                }
            }
        }

        // Verificar si hay preguntas filtradas disponibles
        if (filteredQuestionsList.size() > 0) {
            // Iniciar DashboardActivity con la lista filtrada de preguntas
            Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
            intent.putExtra("questionsList", (ArrayList<ModelClass>) filteredQuestionsList);
            startActivity(intent);
        } else {
            // No hay preguntas disponibles para la categoría seleccionada,
            // manejar el caso aquí
        }
    }

    public static ArrayList<ModelClass> getList() {
        return list;
    }

    // Bloque estático para agregar preguntas al ArrayList list.
    static {
        list = new ArrayList<>();
        ///cultura general

        //Cesur Docencia preguntas Modulo 3
        //tema1

        list.add(new ModelClass("El aprendizaje que se da en un marco de coherencia entre conocimiento previo y conocimiento nuevo es:", "Aprendizaje receptivo", "Aprendizaje por descubrimiento o empírico", "Aprendizaje de memoria", "Aprendizaje significativo", "Aprendizaje significativo", "Sistemas"));
        list.add(new ModelClass("De entre las características del aprendizaje de adultos encontramos la que se refiere a la fuente natural del conocimiento que se refiere a:", "Autonomía", "Motivación", "Experiencia", "El aprendizaje orientado", "Experiencia","Sistemas"));
        list.add(new ModelClass("Las personas realistas tienen un estilo de aprendizaje:", "Activo", "Analítico", "Pragmático", "Reflexivo", "Pragmático","Sistemas"));
        list.add(new ModelClass("La memoria sensorial es:", "La memoria innata de las personas", "La memoria a corto plazo", "La memoria inmediata", "La memoria a largo plazo", "La memoria inmediata","Sistemas"));
        list.add(new ModelClass("¿Con qué tipo de recuerdo u olvido está relacionada la siguiente frase? 'No supe responder a la pregunta 4 ya que como era del primer tema no lo tenía fresco'.", "Olvido por desuso", "Recuerdo de reconocimiento", "Olvido motivado", "Recuerdo de elaboración", "Olvido por desuso","Sistemas"));
        list.add(new ModelClass("La motivación requiere:", "Una necesidad, un impulso y un objetivo", "Un comportamiento, una acción y una necesidad", "Un objetivo, una necesidad y una actitud", "Una necesidad, una actitud y una acción", "Una necesidad, un impulso y un objetivo","Sistemas"));
        list.add(new ModelClass("En el proceso de la conducta motivacional ¿a qué fase nos referimos si la necesidad o carencia ha sido cubierta?", "Fase carencial", "Fase dinámica", "Fase reductora", "Ninguna de las anteriores es correcta", "Fase reductora","Sistemas"));
        list.add(new ModelClass("La fase de comunicación en la que los alumnos preguntan dudas al docente después de la exposición de un tema, se le denomina:", "Mensaje", "Contexto", "Canal", "Feedback", "Feedback","Sistemas"));
        list.add(new ModelClass("La comunicación sincrónica se caracteriza por:", "Es independiente del lugar", "Es independiente del tiempo", "Es dependiente de un tiempo en concreto", "Permite la transferencia de activos en tiempo diferido", "Es dependiente de un tiempo en concreto","Sistemas"));

        //Tema 2
        list.add(new ModelClass("De entre las características del aprendizaje en grupo encontramos: “Se establecen jerarquías entre los miembros del grupo, diferenciando los roles que tengan”. ¿A qué nos referimos?", "Interacción", "Estructura", "Cohesión", "Metas", "Estructura","Programacion"));
        list.add(new ModelClass("Los grupos pueden clasificarse atendiendo a varios criterios: pedagógicos, psicológicos, sociológicos o por tamaño. ¿Con qué grupo puede relacionarse el criterio pedagógico?", "Grupos de referencia", "Sociogrupo", "Grupo de pertenencia", "Grupo base", "Grupo de pertenencia","Programacion"));
        list.add(new ModelClass("La fase en la que cada uno de los integrantes ofrece sus habilidades para apoyar la misión del grupo se llama:", "Fase de apoyo", "Fase de desempeño", "Fase de tormenta", "Fase de formación", "Fase de desempeño","Programacion"));
        list.add(new ModelClass("La coordinación de un grupo de trabajo resulta útil para comprobar el grado de consecución de:", "Objetivos", "Procesos", "Estructuras", "Responsabilidades", "Objetivos","Programacion"));
        list.add(new ModelClass("Dos de los siguientes rasgos y actitudes son característicos del responsable de coordinar y moderar un grupo de trabajo:", "Intervención continua en las actuaciones de un grupo", "Facilitar el acceso a materiales y recursos", "Utilizar un tono de voz enérgico", "Expresarse de forma ambigua", "Facilitar el acceso a materiales y recursos","Programacion"));
        list.add(new ModelClass("El liderazgo autocrático se caracteriza por:", "Falta de coordinación entre el grupo", "Facilitar la responsabilidad compartida", "Alta productividad pero satisfacción inadecuada", "Admitir discusiones en el grupo", "Alta productividad pero satisfacción inadecuada","Programacion"));
        list.add(new ModelClass("El líder inseguro que toma decisiones según deseos individuales, no organiza tareas ni actividades y nadie asume responsabilidades es característico del estilo de liderazgo:", "Autocrático", "Liberal", "Democrático", "No es propio de ningún liderazgo", "Liberal","Programacion"));
        list.add(new ModelClass("En relación a un miembro de un grupo que es un charlatán ¿qué recomendación podríamos hacer al resto del grupo?", "Aprovechar lo positivo de sus intervenciones", "Evitar seguirle la corriente", "Hacerle colaborar en tareas no destacadas", "Comentar en privado su comportamiento", "Evitar seguirle la corriente","Programacion"));
        list.add(new ModelClass("Las tres técnicas principales en la resolución de conflictos son:", "Negociación, mediación y arbitraje", "Negociación, discusión y tormenta de ideas", "Negociación, tormenta de ideas y arbitraje", "Mediación, arbitraje y comprensión", "Negociación, mediación y arbitraje","Programacion"));
        //Tema 3

        list.add(new ModelClass(
                "Desde el punto de vista psicopedagógico, el método de enseñanza basado en la construcción del aprendizaje recibe el nombre de:",
                "Expositivo",
                "Demostrativo",
                "Interrogativo",
                "Activo",
                "Activo",
                "Seguridad"
        ));

        list.add(new ModelClass(
                "Una de las ventajas del método expositivo es:",
                "Lenta transmisión del contenido",
                "Muy eficaz para pequeños grupos",
                "Permite motivar y/o estimular a los participantes",
                "No se complementa fácilmente con otros métodos",
                "Permite motivar y/o estimular a los participantes",
                "Seguridad"
        ));

        list.add(new ModelClass(
                "Señala qué opción NO es una ventaja del método interrogativo:",
                "Favorece la autoevaluación",
                "Estimulan la participación",
                "Favorece la creatividad",
                "Eficaz para trabajar habilidades",
                "Eficaz para trabajar habilidades",
                "Seguridad"
        ));

        list.add(new ModelClass(
                "Los principios metodológicos se centran en:",
                "La adaptación al nivel del alumnado y en el principio de interdependencia",
                "La adaptación al nivel del alumnado y en la participación",
                "La adaptación al nivel del alumnado y en el clima de aprendizaje positivo",
                "La adaptación al nivel del alumno y en los medios y recursos didácticos",
                "La adaptación al nivel del alumno y en los medios y recursos didácticos",
                "Seguridad"
        ));

        list.add(new ModelClass(
                "Señale la proposición incorrecta:",
                "Principio de Actividad afirma que el método permite una actitud de accesibilidad de los participantes a los contenidos",
                "Principio de Funcionalidad es específico de las personas adultas que tienen claro que cualquier aprendizaje al que se enfrentan tiene que tener una proyección directa sobre la consecución de una meta",
                "Principio de la horizontalidad considera a alumnos y profesores como artífices en la construcción de su propio conocimiento",
                "Principio del aprendizaje autónomo es una condición intrínseca de la condición adulta a través de la cual se capacita a las personas para que puedan aprender por sí mismas",
                "Principio de Funcionalidad es específico de las personas adultas que tienen claro que cualquier aprendizaje al que se enfrentan tiene que tener una proyección directa sobre la consecución de una meta",
                "Seguridad"
        ));

        list.add(new ModelClass(
                "Indique un punto a analizar para establecer las estrategias metodológicas:",
                "Los contenidos asociados",
                "Las aportaciones a los foros",
                "Las actividades a realizar",
                "Ninguna de las anteriores es correcta",
                "Las actividades a realizar",
                "Seguridad"
        ));

        list.add(new ModelClass(
                "¿Qué competencia no es propiamente necesaria para un buen docente?",
                "Didáctica",
                "Social",
                "Técnica",
                "Pericial",
                "Pericial" ,
                "Seguridad"));

        list.add(new ModelClass(
                "La mejora de las habilidades docentes está íntimamente relacionada con:",
                "El grado de conocimiento que el docente tenga de la materia",
                "El clima del aula",
                "La eficacia comunicativa",
                "Todas las respuestas son correctas",
                "Todas las respuestas son correctas",
                "Seguridad"
        ));

        list.add(new ModelClass(
                "La técnica de simulación docente que permite al estudiante observar su propia conducta se llama:",
                "Exposición didáctica",
                "Feedback",
                "Microenseñanza",
                "Metaenseñanza",
                "Feedback",
                "Seguridad"
        ));

        list.add(new ModelClass(
                "Qué característica es propia del aula virtual:",
                "La comunicación es vertical docente-alumno",
                "Es fácil la actualización de los contenidos",
                "La comunicación siempre es síncrona",
                "Permite el uso de elementos multimedia",
                "Permite el uso de elementos multimedia",
                "Seguridad"
        ));
        //Tema 4
        list.add(new ModelClass("Capacitar para la auto-orientación es una de las características de:", "La formación mixta", "La acción tutorial", "La formación de demanda", "La formación e-learning", "La acción tutorial","Leyes"));
        list.add(new ModelClass("Una de las siguientes características no es propia de la acción tutorial:", "Priorización y adaptación a las necesidades y contextos concretos", "Atención a las peculiaridades del alumnado", "Asimilación de las indicaciones del centro", "Prevención y detección temprana de dificultades", "Asimilación de las indicaciones del centro","Leyes"));
        list.add(new ModelClass("Indique cuál de las siguientes respuestas no es requisito del aprendizaje autónomo:", "Fomenta la curiosidad, la investigación y la autodisciplina", "Existir motivación intrínseca", "Presentar expectativas claras y bien definidas", "Creatividad para elaborar el conocimiento y lograr el o los objetivos marcados", "Presentar expectativas claras y bien definidas","Leyes"));
        list.add(new ModelClass("Uno de los siguientes rasgos que le mostramos es característico de las personas con un estilo de aprendizaje analítico:", "Realista", "Crítico", "Concienzuda", "Directa", "Concienzuda","Leyes"));
        list.add(new ModelClass("De una persona eficaz, realista, directa y que le gusta experimentar puede decirse que su estilo de aprendizaje es mayoritariamente:", "Activo", "Pragmático", "Analítico", "Reflexivo", "Pragmático","Leyes"));
        list.add(new ModelClass("Marque la característica que no es propia de la comunicación sincrónica:", "Independiente del lugar", "No es accesible a todas las personas", "Se basa en el texto", "Temporalmente dependiente", "No es accesible a todas las personas","Leyes"));
        list.add(new ModelClass("Una de las siguientes proposiciones es una característica del tutor con rol proactivo:", "Conector de los agentes intervinientes en el proceso de formación", "Transmisor de los contenidos", "Facilitador del aprendizaje", "Controlador del alumno para garantizar el correcto desarrollo del proceso formativo", "Facilitador del aprendizaje","Leyes"));
        list.add(new ModelClass("¿Cuál de los siguientes objetivos no está estrechamente relacionado con la habilidad de sensibilización que ha de tener el tutor?", "Centrar al alumnado en la tarea que deba aprender", "Estimular la implicación del alumno/a en dicha tarea", "Crear expectativa sobre la tarea", "Realizar pruebas permanentemente", "Realizar pruebas permanentemente","Leyes"));
        list.add(new ModelClass("El seguimiento del aprendizaje tutorial debe ser:", "Planificado y continuo", "Participativo y efectivo", "Formativo y coordinado", "Todas son correctas", "Todas son correctas","Leyes"));
        //Tema 5
        list.add(new ModelClass("¿Cuál de las siguientes respuestas está referida a las características de los alumnos adultos?", "Los adultos suelen estar mucho más motivados", "Los adultos suelen ser mucho más responsables", "Los adultos quieren satisfacer sus inquietudes en un área concreta", "Todas las respuestas son correctas", "Todas las respuestas son correctas","Leyes"));
        list.add(new ModelClass("Indique cuál es la fase 1 de la temporalización de la acción tutorial:", "Planificación", "Sensibilización", "Programación", "Evaluación", "Planificación","Leyes"));
        list.add(new ModelClass("Una de las actividades que puede recogerse en el Plan de Actuación Individualizado es:", "Detectar las necesidades puntuales e individualizar", "Realizar una evaluación inicial pero no determinar apoyos que pueda necesitar", "Detectar las necesidades puntuales pero no individualizar la evaluación", "Realizar un planning de estudio", "Detectar las necesidades puntuales e individualizar","Leyes"));
        list.add(new ModelClass("En las actividades de seguimiento docente, ¿cuándo se debe llevar a cabo la detección de necesidades?", "Antes del curso", "Durante el curso", "En los tests", "Después del curso", "Antes del curso","Leyes"));

        //Tema 6
        list.add(new ModelClass("¿A qué apartado de la guía del alumno nos referimos cuando hablamos de 'expresados de forma clara y comprensible de tal forma que compendien de manera resumida la competencia general que hay que lograr'?", "Perfil del alumnado", "Requisitos técnicos", "Objetivos generales", "Requisitos funcionales", "Objetivos generales","Leyes"));
        list.add(new ModelClass("El sistema de evaluación final precisa:", "Objetivos", "Trabajos evaluables", "Pruebas finales", "Todas las opciones son correctas", "Todas las opciones son correctas","Leyes"));
        list.add(new ModelClass("Indique qué tipo de cuestionarios tiene Moodle:", "Opción múltiple y de respuesta corta", "Sumativa y formativa", "Emparejamiento y ensayo", "Las opciones 'a' y 'c' son correctas", "Las opciones 'a' y 'c' son correctas","Leyes"));
        list.add(new ModelClass("Seleccione, de entre las siguientes opciones, cuáles son responsabilidad administrativa del tutor:", "Participación en la puesta en marcha inicial de la acción formativa", "Participación en los procesos de mejora de futuras ediciones del curso", "Confección del acta de evaluación", "Todas las opciones son correctas", "Todas las opciones son correctas","Leyes"));
        list.add(new ModelClass("¿En qué fase se narrará dónde se desarrollarán los contenidos?", "Fase de introducción", "Fase de redacción", "Fase de grabación", "Fase de edición", "Fase de introducción","Leyes"));
        list.add(new ModelClass("¿Cuál de las siguientes funciones es propia del Jefe de Estudios?", "Ejercer por delegación la jefatura del personal docente en temas académicos", "Participar en las actividades de formación de profesores", "Presidir la votación para elegir al director", "Elaborar de manera personal los horarios del personal de administración y servicios (PAS)", "Ejercer por delegación la jefatura del personal docente en temas académicos","Leyes"));

/*

        list.add(new ModelClass(
                "¿Cuál de las siguientes afirmaciones es correcta acerca del uso de las TIC en la formación?",
                "a. Las TIC no tienen impacto en el proceso de enseñanza-aprendizaje.",
                "b. Las TIC solo son útiles para los estudiantes, no para los docentes.",
                "c. Las TIC pueden mejorar la accesibilidad, la interactividad y la diversidad en el aprendizaje.",
                "d. Las TIC solo se utilizan en la educación superior, no en niveles inferiores.",
                "c. Las TIC pueden mejorar la accesibilidad, la interactividad y la diversidad en el aprendizaje."
        ));

        list.add(new ModelClass(
                "¿Qué tipo de recurso se utiliza comúnmente para la creación de presentaciones multimedia en el ámbito educativo?",
                "a. Texto impreso en papel.",
                "b. Proyectores de diapositivas.",
                "c. Software de edición de vídeo.",
                "d. Software de presentación como PowerPoint.",
                "d. Software de presentación como PowerPoint."
        ));

        list.add(new ModelClass(
                "¿Cuál de las siguientes opciones es un ejemplo de plataforma virtual de aprendizaje?",
                "a. Microsoft Word.",
                "b. WhatsApp.",
                "c. Moodle.",
                "d. Facebook.",
                "c. Moodle."
        ));

        list.add(new ModelClass(
                "¿Qué es el aprendizaje colaborativo?",
                "a. Un proceso de aprendizaje individual basado en la memorización de información.",
                "b. Un enfoque que excluye la participación de los demás estudiantes.",
                "c. Un método en el que los estudiantes trabajan juntos para lograr objetivos comunes y construir conocimiento de forma colaborativa.",
                "d. Un enfoque que solo se utiliza en entornos virtuales de aprendizaje.",
                "c. Un método en el que los estudiantes trabajan juntos para lograr objetivos comunes y construir conocimiento de forma colaborativa."
        ));

        list.add(new ModelClass(
                "¿Cuál es uno de los beneficios del uso de recursos multimedia en el aula?",
                "a. Aumentar la complejidad y dificultad de los contenidos.",
                "b. Limitar la participación de los estudiantes.",
                "c. Facilitar la comprensión y retención de la información.",
                "d. Reducir la necesidad de adaptar el contenido a diferentes estilos de aprendizaje.",
                "c. Facilitar la comprensión y retención de la información."
        ));
        list.add(new ModelClass(
                "¿Cuál es uno de los principales roles del docente en el proceso de enseñanza-aprendizaje?",
                "a. Facilitar el acceso a los recursos educativos.",
                "b. Realizar actividades administrativas.",
                "c. Evaluar únicamente el conocimiento adquirido.",
                "d. Limitar la participación de los estudiantes.",
                "a. Facilitar el acceso a los recursos educativos."
        ));

        list.add(new ModelClass(
                "¿Qué aspecto se debe tener en cuenta al planificar una sesión formativa?",
                "a. Los intereses personales del docente.",
                "b. La duración máxima de la sesión.",
                "c. La disponibilidad de recursos tecnológicos.",
                "d. Excluir la participación activa de los estudiantes.",
                "c. La disponibilidad de recursos tecnológicos."
        ));

        list.add(new ModelClass(
                "¿Cuál es uno de los objetivos de la evaluación formativa?",
                "a. Calificar y clasificar a los estudiantes.",
                "b. Fomentar la competencia entre los estudiantes.",
                "c. Proporcionar retroalimentación para mejorar el aprendizaje.",
                "d. Establecer una jerarquía entre los estudiantes.",
                "c. Proporcionar retroalimentación para mejorar el aprendizaje."
        ));

        list.add(new ModelClass(
                "¿Qué estrategia puede utilizar el docente para fomentar la participación activa de los estudiantes en el aula?",
                "a. Promover la memorización de conceptos.",
                "b. Utilizar únicamente métodos expositivos.",
                "c. Fomentar la colaboración y el trabajo en equipo.",
                "d. Desestimar las opiniones y preguntas de los estudiantes.",
                "c. Fomentar la colaboración y el trabajo en equipo."
        ));

        list.add(new ModelClass(
                "¿Cuál es una ventaja de utilizar recursos audiovisuales en el proceso de enseñanza-aprendizaje?",
                "a. Aumentar la dificultad de comprensión de los contenidos.",
                "b. Limitar la participación de los estudiantes.",
                "c. Favorecer la retención y comprensión de la información.",
                "d. Reducir la necesidad de adaptar el contenido a diferentes estilos de aprendizaje.",
                "c. Favorecer la retención y comprensión de la información."
        ));

        list.add(new ModelClass(
                "¿Cuál es el objetivo principal de la retroalimentación en el proceso de enseñanza-aprendizaje?",
                "a. Calificar a los estudiantes.",
                "b. Motivar a los estudiantes.",
                "c. Proporcionar información para mejorar el aprendizaje.",
                "d. Comparar a los estudiantes con sus compañeros.",
                "c. Proporcionar información para mejorar el aprendizaje."
        ));

        list.add(new ModelClass(
                "¿Cuál de las siguientes afirmaciones describe mejor la enseñanza centrada en el estudiante?",
                "a. El docente es el único responsable de la transmisión de conocimientos.",
                "b. Los estudiantes deben seguir instrucciones sin hacer preguntas.",
                "c. Los estudiantes son activos en su propio aprendizaje y participan en la toma de decisiones.",
                "d. Los estudiantes deben adaptarse al estilo de enseñanza del docente.",
                "c. Los estudiantes son activos en su propio aprendizaje y participan en la toma de decisiones."
        ));

        list.add(new ModelClass(
                "¿Cuál de las siguientes opciones describe mejor el concepto de aprendizaje autónomo?",
                "a. Los estudiantes aprenden únicamente a través de la interacción con el docente.",
                "b. Los estudiantes deben seguir instrucciones sin cuestionarlas.",
                "c. Los estudiantes son responsables de su propio aprendizaje y toman la iniciativa en su proceso de aprendizaje.",
                "d. Los estudiantes dependen completamente de los recursos digitales para aprender.",
                "c. Los estudiantes son responsables de su propio aprendizaje y toman la iniciativa en su proceso de aprendizaje."
        ));

        list.add(new ModelClass(
                "¿Qué es la evaluación formativa?",
                "a. Una evaluación final que determina la calificación del estudiante.",
                "b. Una evaluación que se realiza al final de cada unidad temática.",
                "c. Una evaluación que proporciona retroalimentación y guía para mejorar el aprendizaje durante el proceso educativo.",
                "d. Una evaluación que se realiza únicamente en entornos virtuales de aprendizaje.",
                "c. Una evaluación que proporciona retroalimentación y guía para mejorar el aprendizaje durante el proceso educativo."
        ));

        list.add(new ModelClass(
                "¿Cuál de las siguientes opciones describe mejor el término 'flipped classroom' o aula invertida?",
                "a. Los estudiantes enseñan al docente en lugar de recibir instrucción.",
                "b. Los estudiantes aprenden en entornos virtuales sin la presencia del docente.",
                "c. Los estudiantes acceden a los contenidos y materiales de aprendizaje antes de la clase, y la clase se utiliza para actividades prácticas y de aplicación.",
                "d. Los estudiantes reciben instrucción exclusivamente a través de recursos multimedia.",
                "c. Los estudiantes acceden a los contenidos y materiales de aprendizaje antes de la clase, y la clase se utiliza para actividades prácticas y de aplicación."
        ));
*/


    }

    /**
     * Método debugPrintQuestions para imprimir las preguntas en el log de depuración.
     * @param questions ArrayList de preguntas a imprimir.
     */
    private void debugPrintQuestions(ArrayList<ModelClass> questions) {
        Log.d("Debug", "List size: " + questions.size());
        for (ModelClass question : questions) {
            Log.d("Debug", "Question: " + question.getQuestion());
        }
    }
}