#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_bloomIntensity;
uniform float u_bloomThreshold;

void main() {
    vec4 color = texture2D(u_texture, v_texCoords);

    // Extract bright areas for bloom
    float brightness = dot(color.rgb, vec3(0.2126, 0.7152, 0.0722));
    float contribution = max(0.0, brightness - u_bloomThreshold);
    contribution = contribution * contribution; // Square for smoother falloff

    // Apply bloom effect
    vec3 bloomColor = color.rgb * contribution * u_bloomIntensity;

    // Add bloom to original color
    gl_FragColor = vec4(color.rgb + bloomColor, color.a);
}
