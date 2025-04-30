#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_colorGradingIntensity;

// Color grading parameters
const vec3 brightness = vec3(0.05, 0.05, 0.05);
const vec3 contrast = vec3(1.1, 1.1, 1.1);
const vec3 saturation = vec3(1.2, 1.2, 1.2);
const vec3 tint = vec3(1.0, 0.9, 0.8); // Warm tint

void main() {
    vec4 color = texture2D(u_texture, v_texCoords);

    // Apply brightness
    vec3 graded = color.rgb + brightness;

    // Apply contrast
    graded = ((graded - 0.5) * contrast) + 0.5;

    // Apply saturation
    float luminance = dot(graded, vec3(0.2126, 0.7152, 0.0722));
    graded = mix(vec3(luminance), graded, saturation);

    // Apply tint
    graded *= tint;

    // Mix with original based on intensity
    color.rgb = mix(color.rgb, graded, u_colorGradingIntensity);

    gl_FragColor = color;
}
