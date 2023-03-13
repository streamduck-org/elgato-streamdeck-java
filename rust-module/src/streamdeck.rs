use elgato_streamdeck::info::Kind;
use elgato_streamdeck::{list_devices, StreamDeck, StreamDeckError};
use hidapi::HidApi;
use jni::JNIEnv;
use jni::objects::{JClass, JObject, JString, JValue, JValueGen};
use jni::sys::{jbyte, jint, jlong};

fn convert_kind(kind: Kind) -> &'static str {
    match kind {
        Kind::Original => "ORIGINAL",
        Kind::OriginalV2 => "ORIGINAL_V2",
        Kind::Mini => "MINI",
        Kind::Xl => "XL",
        Kind::XlV2 => "XL_V2",
        Kind::Mk2 => "MK2",
        Kind::MiniMk2 => "MINI_MK2",
        Kind::Pedal => "PEDAL"
    }
}

fn convert_java_kind(ordinal: jint) -> Option<Kind> {
    match ordinal {
        0 => Some(Kind::Original),
        1 => Some(Kind::OriginalV2),
        2 => Some(Kind::Mini),
        3 => Some(Kind::Xl),
        4 => Some(Kind::XlV2),
        5 => Some(Kind::Mk2),
        6 => Some(Kind::MiniMk2),
        7 => Some(Kind::Pedal),
        _ => None
    }
}

#[no_mangle]
pub unsafe extern "C" fn Java_org_streamduck_elgato_1streamdeck_StreamDeckInterface_listDevices<'local>(
    mut env: JNIEnv<'local>,
    _class: JClass<'local>,
    hid_pointer: jlong
) -> JObject<'local> {
    let hidapi = &*(hid_pointer as *mut HidApi);

    let java_arraylist = env.new_object("java/util/ArrayList", "()V", &[])
        .expect("Couldn't create an arraylist");

    for (kind, serial) in list_devices(hidapi) {
        let java_kind = env.get_static_field(
            "org/streamduck/elgato_streamdeck/info/DeviceKind",
            convert_kind(kind),
            "Lorg/streamduck/elgato_streamdeck/info/DeviceKind;"
        ).expect("Couldn't find DeviceKind enum field");

        let java_serial = env.new_string(serial)
            .expect("Couldn't create string");

        let java_descriptor = env.new_object(
            "org/streamduck/elgato_streamdeck/DeviceDescriptor",
            "(Lorg/streamduck/elgato_streamdeck/info/DeviceKind;Ljava/lang/String;)V",
            &[java_kind.borrow(), JValue::from(&java_serial)]
        ).expect("Couldn't create descriptor");

        env.call_method(&java_arraylist, "add", "(Ljava/lang/Object;)Z", &[JValue::from(&java_descriptor)])
            .expect("Couldn't add descriptor to the array");
    }

    java_arraylist
}

#[no_mangle]
pub unsafe extern "C" fn Java_org_streamduck_elgato_1streamdeck_StreamDeckInterface_connectStreamDeck<'local>(
    mut env: JNIEnv<'local>,
    _class: JClass<'local>,
    hid_pointer: jlong,
    java_kind: JObject<'local>,
    java_serial: JString<'local>
) -> jlong {
    let hidapi = &*(hid_pointer as *mut HidApi);

    let JValueGen::Int(java_kind_ordinal) = env.call_method(&java_kind, "ordinal", "()I", &[])
        .expect("Couldn't retrieve ordinal of the DeviceKind enum") else {
        panic!("Couldn't get DeviceKind ordinal integer");
    };

    let kind = convert_java_kind(java_kind_ordinal).expect("Couldn't map DeviceKind to rust Kind");

    let serial_string = String::from(env.get_string(&java_serial)
        .expect("Couldn't retrieve serial number"));

    match StreamDeck::connect(hidapi, kind, &serial_string) {
        Ok(connection) => {
            let boxed = Box::new(connection);

            Box::into_raw(boxed) as jlong
        }

        Err(err) => {
            let _ = env.throw_new("java/lang/RuntimeException", err.to_string());

            0
        }
    }
}

#[no_mangle]
pub unsafe extern "C" fn Java_org_streamduck_elgato_1streamdeck_StreamDeckInterface_freeStreamDeck<'local>(
    mut _env: JNIEnv<'local>,
    _class: JClass<'local>,
    streamdeck_addr: jlong,
) {
    let _ = Box::from_raw(streamdeck_addr as *mut StreamDeck);
}

#[allow(unreachable_patterns)]
fn handle_error<'local, T>(env: &mut JNIEnv<'local>, result: Result<T, StreamDeckError>) -> Option<T> {
    match result {
        Ok(v) => {
            return Some(v)
        }
        Err(err) => match err {
            StreamDeckError::HidError(hid) => {
                env.throw_new("org/streamduck/elgato_streamdeck/exceptions/HIDException", hid.to_string())
            }
            StreamDeckError::Utf8Error(utf) => {
                env.throw_new("java/lang/RuntimeException", utf.to_string())
            }
            StreamDeckError::ImageError(img) => {
                env.throw_new("java/lang/RuntimeException", img.to_string())
            }
            StreamDeckError::NoScreen => {
                env.throw_new("org/streamduck/elgato_streamdeck/exceptions/ThereIsNoScreenException", "There's no screen on the device")
            }
            StreamDeckError::InvalidKeyIndex => {
                env.throw_new("org/streamduck/elgato_streamdeck/exceptions/InvalidKeyIndexException", "Provided key index is out of bounds")
            }
            StreamDeckError::UnrecognizedPID => {
                env.throw_new("org/streamduck/elgato_streamdeck/exceptions/UnrecognizedPIDException", "Provided PID didn't work, how did you get this error??")
            }
            _ => {
                env.throw_new("java/lang/RuntimeException", "Unknown error")
            }
        },
    }.expect("Couldn't throw exception");

    None
}

#[no_mangle]
pub unsafe extern "C" fn Java_org_streamduck_elgato_1streamdeck_StreamDeckInterface_setBrightness<'local>(
    mut env: JNIEnv<'local>,
    _class: JClass<'local>,
    streamdeck_addr: jlong,
    brightness: jbyte
) {
    let streamdeck = &*(streamdeck_addr as *mut StreamDeck);

    handle_error(&mut env, streamdeck.set_brightness(brightness as u8));
}

#[no_mangle]
pub unsafe extern "C" fn Java_org_streamduck_elgato_1streamdeck_StreamDeckInterface_reset<'local>(
    mut env: JNIEnv<'local>,
    _class: JClass<'local>,
    streamdeck_addr: jlong
) {
    let streamdeck = &*(streamdeck_addr as *mut StreamDeck);

    handle_error(&mut env, streamdeck.reset());
}