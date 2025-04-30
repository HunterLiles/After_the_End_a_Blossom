#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_vignetteIntensity;
uniform vec2 u_resolution;

void main() {
    vec4 color = texture2D(u_texture, v_texCoords);

    // Calculate normalized coordinates from center
    vec2 position = (v_texCoords - 0.5) * 2.0;

    // Calculate vignette factor
    float len = length(position);
    float vignette = smoothstep(0.8, 0.2, len * u_vignetteIntensity);

    // Apply vignette
    color.rgb = color.rgb * vignette;

    gl_FragColor = color;
}
